package com.shatteredpixel.shatteredpixeldungeon.items.thanks;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class DistressSignalNesting extends Item implements Item.AnimationItem {

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
    public void frames(ItemSprite itemSprite){
        if(animation) {
            itemSprite.texture(Assets.Sprites.ANIMATIONS_SOS);
            TextureFilm frames = new TextureFilm(itemSprite.texture, 16, 16);
            MovieClip.Animation idle = new MovieClip.Animation(14, true);
            switch (level()){
                default:
                case 0:
                    idle.frames( frames,0,0,1,1,2,2,2,3,3,4,4,5,5);
                    break;
                case 1:
                    idle.frames( frames,7,7,8,8,9,9,10,10,11,11,12,12,13,13);
                    break;
                case 2:
                    idle.frames( frames,14,14,15,15,16,16,17,17,18,18,19,19,20,20);
                    break;
                case 3:
                    idle.frames( frames,21,21,22,22,23,23,24,24,25,25,26,26,27,27);
                    break;
            }
            itemSprite.play(idle);
        } else {
            itemSprite.view(image(),glowing());
        }
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
