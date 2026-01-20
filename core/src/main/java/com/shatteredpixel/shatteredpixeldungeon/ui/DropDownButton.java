package com.shatteredpixel.shatteredpixeldungeon.ui;

import com.shatteredpixel.shatteredpixeldungeon.Chrome;
import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
import com.watabou.input.PointerEvent;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.PointerArea;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Signal;

public class DropDownButton extends StyledButton{
    private final Signal<String> toggleSignal =new Signal<>();
    private String[] options;
    private int backupGroundColor;
    private int selectedOptionColor;
    private DropDownPanel dropDownPanel;
    private boolean shouldShowMoreOption = false;

    public DropDownButton(String[] options){
        super(Chrome.Type.WINDOW,"");
        this.options = options;

        if( text.text().isEmpty() )
            text.text( this.options[0] );
        icon( Icons.get( Icons.UNCHECKED ) );
    }

    public DropDownButton(String[] options, boolean shouldShowMoreOption){
        super(Chrome.Type.WINDOW,"");
        this.options = options;
        this.shouldShowMoreOption = shouldShowMoreOption;

        if( text.text().isEmpty() )
            text.text( this.options[0] );
        icon( Icons.get( Icons.UNCHECKED ) );
    }

    public DropDownButton(String initialText,String[] options) {
        super(Chrome.Type.WINDOW,initialText);
        this.options = options;
        icon( Icons.get( Icons.UNCHECKED ) );
    }

    public DropDownButton(String initialText,String[] options, boolean shouldShowMoreOption) {
        super(Chrome.Type.WINDOW,initialText);
        this.options = options;
        this.shouldShowMoreOption = shouldShowMoreOption;
        icon( Icons.get( Icons.UNCHECKED ) );
    }

    public DropDownButton(String initialText,String[] options, int size) {
        super(Chrome.Type.WINDOW,initialText, size);
        this.options = options;
        icon( Icons.get( Icons.UNCHECKED ) );
    }

    public DropDownButton(String initialText,String[] options, int size, boolean shouldShowMoreOption) {
        super(Chrome.Type.WINDOW,initialText, size);
        this.options = options;
        this.shouldShowMoreOption = shouldShowMoreOption;
        icon( Icons.get( Icons.UNCHECKED ) );
    }

    @Override
    protected void layout() {
        super.layout();

        float margin = (height - text.height()) / 2;

        text.setPos( x + margin, y + margin);
        PixelScene.align(text);

        margin = (height - icon.height) / 2;

        icon.x = x + width - margin - icon.width;
        icon.y = y + margin;
        PixelScene.align(icon);
    }

    @Override
    protected void onClick() {
        super.onClick();
        icon.copy( Icons.get( dropDownPanel == null ? Icons.CHECKED : Icons.UNCHECKED ) );
        if(dropDownPanel!=null) {
            hideDropDownPanel();
            return;
        }

        toggleSignal.add(new Signal.Listener<String>() {
            @Override
            public boolean onSignal(String s) {
                onOptionSelected(s);
                return false;
            }
        });

        dropDownPanel = new DropDownPanel();
        add(dropDownPanel);
        dropDownPanel.setRect(this.left(), this.bottom(), width, height);
        dropDownPanel.setPos( this.left(), this.bottom() );
        dropDownPanel.updateList( this.options,text(), toggleSignal ,shouldShowMoreOption );
    }

    protected void onOptionSelected(String option){
        this.text(option);
        icon.copy( Icons.get( dropDownPanel == null ? Icons.UNCHECKED : Icons.CHECKED ) );
        hideDropDownPanel();
    }

    protected void hideDropDownPanel(){
        if(dropDownPanel!=null) {
            dropDownPanel.killAndErase();
            dropDownPanel = null;
        }
    }

    public static class DropDownPanel extends Component{
        private ScrollingListPane listPane;
        protected NinePatch bg;

        @Override
        protected void createChildren() {
            bg = Chrome.get(Chrome.Type.WINDOW_SILVER);
            addToBack(bg);
            listPane = new ScrollingListPane();
            add( listPane );
        }

        @Override
        protected void layout() {
            super.layout();
            bg.x = x;
            bg.y = y;
            bg.size( width, height );
            listPane.setRect( x, y, width, height);
        }

        public void updateList(String[] options,String defaultOption,Signal<String> listener,boolean shouldShowMoreOption){
            if( options.length == 0 ) {
                ScrollingListPane.ListItem item = new ScrollingListPane.ListItem(null, null, "");
                listPane.addItem(item);
            }

            int totalHeight = 0;

            for (String displayOption : options){
                ScrollingListPane.ListItem item = new ScrollingListPane.ListItem(null, null, displayOption){
                    @Override
                    public boolean onClick(float x, float y) {
                        if ( inside( x, y ) ) {
                            listener.dispatch( displayOption );
                            listPane.destroy();
                            bg.remove();
                            return true;
                        } else {
                            listPane.destroy();
                            bg.remove();
                            return false;
                        }
                    }
                };
                if(displayOption.equals(defaultOption))
                    item.hardlight(0xFFD0D0D0);
                listPane.addItem(item);
                totalHeight += item.height();
            }
            if (shouldShowMoreOption) {
                listPane.setRect(x, y, width, totalHeight);
                bg.size( width, totalHeight );
            } else {
                listPane.setRect(x, y, width, height);
                bg.size( width, height );
            }
        }
    }
}
