package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class DistressSignalNesting extends Item {

    {
        image = ItemSpriteSheet.SOS_0;
        animation = true;
    }

    public int image() {
        switch (level()){
            default:
            case 0:
                super.image = ItemSpriteSheet.SOS_0;
                break;
            case 1:
                super.image = ItemSpriteSheet.SOS_1;
                break;
            case 2:
                super.image = ItemSpriteSheet.SOS_2;
                break;
            case 3:
                super.image = ItemSpriteSheet.SOS_3;
                break;
        }
        return image;
    }

    @Override
    public String desc() {
        String desc = super.desc();
        switch (level()){
            default:
            case 0:
                desc +="\n\n"+Messages.get(this,"level_0");
                break;
            case 1:
                desc +="\n\n"+ Messages.get(this,"level_1");
                break;
            case 2:
                desc += "\n\n"+Messages.get(this,"level_2");
                break;
            case 3:
                desc +="\n\n"+ Messages.get(this,"level_3");
                break;
        }
        return desc;
    }


}
