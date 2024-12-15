package com.kingtyphon.kaijucraft.item.exam;

import com.kingtyphon.kaijucraft.capabilities.KaijuCapability;
import com.kingtyphon.kaijucraft.capabilities.KaijuProvider;
import com.kingtyphon.kaijucraft.networking.ModMessages;
import com.kingtyphon.kaijucraft.networking.packets.KaijuPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Random;

public class AcceptanceLetterItem extends Item {
    public AcceptanceLetterItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {

        pPlayer.getCapability(KaijuProvider.KAIJU_CAPABILITY).ifPresent(kapability ->{
            Random random = new Random();
            int chance = random.nextInt(112) + 1;
            if (chance <= 7) {
                kapability.setLevel(randomCombatPower(0, 4)); // range for 0%-4% CPP
            }
            if ( chance <= 97 || chance > 7) {
                kapability.setLevel(randomCombatPower(5, 20)); // range for 0%-4% CPP
            }
            if (chance <= 107 || chance> 97) {
                kapability.setLevel(randomCombatPower(21, 39)); // range for 0%-4% CPP
            }
            if (chance <= 112 || chance> 97) {
                kapability.setLevel(randomCombatPower(40 , 50)); // range for 0%-4% CPP
            }
            kapability.setSP(getSPChange(kapability.getLevel()));
            kapability.setXP(0);
            kapability.setMelee(0);
            kapability.setMind(0);
            kapability.setRange(0);
            ModMessages.send(new KaijuPacket(kapability), (ServerPlayer) pPlayer);

                    //Changes the flag for being a Kaiju Defense Force Member

        }
        );
        return super.use(pLevel, pPlayer, pUsedHand);

    } private int randomCombatPower(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min; // Generates a random number between min and max (inclusive)
    }
    private int getSPChange(int level){
        if(level <5){
        return 1;
        }
        if(level >= 5 && level <8){
            return 2;
        }
        if(level >= 8 && level <11){
            return 3;
        }
        if(level >= 11 && level < 14){
            return 4;
        }if(level >= 14 && level < 18){
            return 5;
        }
        if(level >= 18 && level < 22){
            return 6;
        }
        if(level >= 22 && level < 26){
            return 7;
        }
        if(level >= 26 && level < 34){
            return 8;
        }
        if(level >= 34 && level < 42){
            return 9;
        }
        if(level >= 42 && level < 45){
            return 10;
        }
        if(level >= 45 && level < 51){
            return 11;
        }
        if(level >= 51 && level < 54){
            return 12;
        }if(level >= 54 && level < 57){
            return 13;
        }if(level >= 57 && level < 59){
            return 14;
        }if(level >= 59 && level < 61){
            return 15;
        }if(level >= 61 && level < 64){
            return 16;
        }if(level >= 64 && level < 69){
            return 17;
        }if(level >= 69 && level < 75){
            return 18;
        }if(level >= 75 && level < 78){
            return 19;
        }
        if(level >= 78 && level < 81){
            return 20;
        }if(level >= 81 && level < 84){
            return 21;
        }if(level >= 84 && level < 87){
            return 22;
        }if(level >= 87 && level < 93){
            return 23;
        }if(level >= 93 && level < 95){
            return 24;
        }if(level >= 95 && level < 98){
            return 25;
        }
        else return 0;
    }
}
