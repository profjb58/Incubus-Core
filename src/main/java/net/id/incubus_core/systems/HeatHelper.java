package net.id.incubus_core.systems;

import net.id.incubus_core.mixin.world.BiomeAccessor;
import net.id.incubus_core.util.RandomShim;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Collections;

public class HeatHelper {

    public static double exchangeHeat(Material medium, double tempA, double tempB, double exchangeArea) {
        return medium.heatConductivity() * exchangeArea * (tempA - tempB);
    }

    public static double playerAmbientHeat(Material medium, double playerTemp, BlockPos playerPos, Biome biome, boolean night, boolean rain) {
        return -exchangeHeat(medium, playerTemp, translateBiomeHeat(playerPos, biome, night, rain), 4.5);
    }

    public static double simulateAmbientHeating(HeatIo io, World world, BlockPos pos, Simulation simulation) {
        var validExchangeDirs = io.getValidDirections();

        if(!validExchangeDirs.isEmpty()) {
            Collections.shuffle(validExchangeDirs, new RandomShim(world.getRandom()));
            var exchangeDir = io.getPreferredDirection().orElse(validExchangeDirs.get(0));

            if(validExchangeDirs.contains(exchangeDir)) {
                double transfer = exchangeHeat(DefaultMaterials.AIR, io.getTemperature(), translateBiomeHeat(pos, world.getBiome(pos).value(), world.isNight(), world.isRaining()), io.getExchangeArea(exchangeDir));
                if(simulation == Simulation.ACT) {
                    io.cool(transfer);
                }
                return transfer;
            }
        }
        return 0;
    }

    public static double translateBiomeHeat(BlockPos pos, Biome biome, boolean night, boolean rain) {

        double baseTemp = biome.getTemperature();
        /*FIXME They remove Biome categories from what I can tell. This is just to make it compile for now.
        Biome.Category category = ((BiomeAccessor) (Object) biome).getCategory();

        double temp = switch (category) {
            case NETHER -> baseTemp + 1;
            case THEEND -> baseTemp - 1.5;
            case DESERT -> baseTemp - 0.25;
            case OCEAN -> baseTemp + 0.1;
            default -> baseTemp;
        };
         */
        double temp = baseTemp;

        temp *= 26.25;

        if(night) {
            if(/*category == Biome.Category.DESERT || category == Biome.Category.MESA*/ false) {
                temp -= temp * 0.75;
            } else if(biome.getPrecipitation() == Biome.Precipitation.SNOW) {
                if(/*category == Biome.Category.ICY*/ false && temp < 0)
                    temp += temp * 1.25;
                else
                    temp -= 20;

            } else {
                temp -= (biome.getPrecipitation() == Biome.Precipitation.NONE) ? 12 : 4;
            }
        }
        if(rain) {
            temp -= biome.getPrecipitation() == Biome.Precipitation.SNOW || biome.getPrecipitation() == Biome.Precipitation.RAIN && pos.getY() >= 100 ? 10 : 3;
        }
        return temp;
    }
}
