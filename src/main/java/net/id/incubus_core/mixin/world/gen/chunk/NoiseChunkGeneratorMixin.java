package net.id.incubus_core.mixin.world.gen.chunk;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.id.incubus_core.util.SeedSupplier;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin extends ChunkGenerator {
	@Unique private static long LAST_SEED = SeedSupplier.MARKER;

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public NoiseChunkGeneratorMixin(Registry<StructureSet> registry, Optional<RegistryEntryList<StructureSet>> optional, BiomeSource biomeSource) {
		super(registry, optional, biomeSource);
	}

	/*TODO Does this need to be moved somewhere? NoiseChunkGenerator doesn't seem to handle seeds anymore.
	@Redirect(
		method = "method_28550(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/kinds/App;",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/serialization/codecs/PrimitiveCodec;fieldOf(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;",
			ordinal = 0
		)
	)
	private static MapCodec<Long> giveUsRandomSeeds(PrimitiveCodec<Long> codec, final String name) {
		return codec.fieldOf(name).orElseGet(SeedSupplier::getSeed);
	}

	@ModifyVariable(
		method = "<init>(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/registry/Registry;Lnet/minecraft/world/biome/source/BiomeSource;Lnet/minecraft/world/biome/source/BiomeSource;JLnet/minecraft/util/registry/RegistryEntry;)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/world/gen/chunk/NoiseChunkGenerator;seed:J",
			opcode = Opcodes.PUTFIELD,
			shift = At.Shift.AFTER
		),
		ordinal = 0,
		argsOnly = true
	)
	private long replaceSeed(long seed) {
		if (seed == SeedSupplier.MARKER) {
			return LAST_SEED;
		} else {
			LAST_SEED = seed;
		}

		return seed;
	}
	 */
}
