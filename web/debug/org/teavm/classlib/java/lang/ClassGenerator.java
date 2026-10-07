package org.teavm.classlib.java.lang;

import org.teavm.dependency.DependencyAgent;
import org.teavm.dependency.DependencyPlugin;
import org.teavm.dependency.MethodDependency;
import org.teavm.model.CallLocation;
import org.teavm.model.ClassReader;
import org.teavm.model.ElementModifier;
import org.teavm.model.FieldReader;
import org.teavm.model.MethodDescriptor;
import org.teavm.model.MethodReference;
import org.teavm.model.ValueType;
import org.teavm.runtime.reflect.ClassInfo;

/**
 * Debug shadow of TeaVM 0.14.0 ClassGenerator.
 *
 * Identical to the stock implementation except reachGetEnumConstants prints
 * the class name whenever the class reader is missing (which would NPE in
 * 0.14.0) before returning. Used only to diagnose the build-time crash.
 */
public class ClassGenerator implements DependencyPlugin {
    private static final MethodDescriptor CLINIT = new MethodDescriptor("<clinit>", void.class);

    @Override
    public void methodReached(DependencyAgent agent, MethodDependency method) {
        switch (method.getReference().getName()) {
            case "getSuperclass":
                reachGetSuperclass(agent, method);
                break;
            case "getInterfaces":
                reachGetInterfaces(agent, method);
                break;
            case "getComponentType":
                reachGetComponentType(agent, method);
                break;
            case "getDeclaredClasses":
                reachGetDeclaredClasses(agent, method);
                break;
            case "getEnumConstants":
                reachGetEnumConstants(agent, method);
                break;
            case "newInstance": {
                var location = new CallLocation(method.getReference());
                var initNewInstanceMethod = new MethodReference(ClassInfo.class, "initializeNewInstance",
                        Object.class, boolean.class);
                method.getVariable(0).getClassValueNode().addConsumer(type -> {
                    if (!(type.getValueType() instanceof ValueType.Object)) {
                        return;
                    }
                    var className = ((ValueType.Object) type.getValueType()).getClassName();
                    var cls = agent.getClassSource().get(className);
                    if (cls != null && !cls.hasModifier(ElementModifier.ABSTRACT)
                            && !cls.hasModifier(ElementModifier.INTERFACE)) {
                        var ref = new MethodReference(className, "<init>", ValueType.VOID);
                        var methodDep = agent.linkMethod(ref);
                        methodDep.addLocation(new CallLocation(initNewInstanceMethod));
                        methodDep.addLocation(location);
                        methodDep.getVariable(0).propagate(type);
                        methodDep.use();
                        method.getResult().propagate(type);
                    }
                });
                break;
            }
        }
    }

    private void reachGetSuperclass(DependencyAgent agent, MethodDependency method) {
        method.getVariable(0).getClassValueNode().addConsumer(type -> {
            if (!(type.getValueType() instanceof ValueType.Object)) {
                return;
            }
            var className = ((ValueType.Object) type.getValueType()).getClassName();
            var cls = agent.getClassSource().get(className);
            if (cls != null && cls.getParent() != null) {
                method.getResult().getClassValueNode().propagate(agent.getType(ValueType.object(cls.getParent())));
            }
        });
    }

    private void reachGetInterfaces(DependencyAgent agent, MethodDependency method) {
        method.getVariable(0).getClassValueNode().addConsumer(type -> {
            if (!(type.getValueType() instanceof ValueType.Object)) {
                return;
            }
            var className = ((ValueType.Object) type.getValueType()).getClassName();
            var cls = agent.getClassSource().get(className);
            method.getResult().propagate(agent.getType(ValueType.arrayOf(ValueType.object("java.lang.Class"))));
            method.getResult().getArrayItem().propagate(agent.getType(ValueType.object("java.lang.Class")));
            if (cls != null) {
                for (String iface : cls.getInterfaces()) {
                    method.getResult().getArrayItem().getClassValueNode().propagate(agent.getType(
                            ValueType.object(iface)));
                }
            }
        });
    }

    private void reachGetComponentType(DependencyAgent agent, MethodDependency method) {
        method.getVariable(0).getClassValueNode().addConsumer(t -> {
            if (!(t.getValueType() instanceof ValueType.Array)) {
                return;
            }
            var itemType = ((ValueType.Array) t.getValueType()).getItemType();
            method.getResult().getClassValueNode().propagate(agent.getType(itemType));
        });
    }

    private void reachGetEnumConstants(DependencyAgent agent, MethodDependency method) {
        method.getResult().propagate(agent.getType(ValueType.parse(Enum[].class)));
        method.getVariable(0).getClassValueNode().addConsumer(t -> {
            if (!(t.getValueType() instanceof ValueType.Object)) {
                return;
            }
            var className = ((ValueType.Object) t.getValueType()).getClassName();
            var cls = agent.getClassSource().get(className);
            if (cls == null) {
                System.err.println("TEAVM-DEBUG missing class reader: " + className);
                return;
            }
            if (!cls.hasModifier(ElementModifier.ENUM)) {
                return;
            }
            if (cls.getMethod(CLINIT) != null) {
                agent.linkMethod(new MethodReference(cls.getName(), CLINIT)).use();
            }
            for (var field : cls.getFields()) {
                if (field.hasModifier(ElementModifier.STATIC) && field.hasModifier(ElementModifier.FINAL)) {
                    agent.linkField(field.getReference()).getValue().connect(method.getResult().getArrayItem());
                }
            }
        });
    }

    private void reachGetDeclaredClasses(DependencyAgent agent, MethodDependency method) {
        method.getResult().propagate(agent.getType(ValueType.parse(Class[].class)));
        method.getResult().getArrayItem().propagate(agent.getType(ValueType.parse(Class.class)));
        method.getVariable(0).getClassValueNode().addConsumer(type -> {
            if (!(type.getValueType() instanceof ValueType.Object)) {
                return;
            }
            var className = ((ValueType.Object) type.getValueType()).getClassName();
            var cls = agent.getClassSource().get(className);
            if (cls != null && cls.getParent() != null) {
                for (var innerClasses : cls.getInnerClasses()) {
                    method.getResult().getArrayItem().getClassValueNode()
                            .propagate(agent.getType(ValueType.object(innerClasses)));
                }
            }
        });
    }
}
