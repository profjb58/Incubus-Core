package net.id.incubus_core.condition;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.id.incubus_core.IncubusCore;
import net.id.incubus_core.condition.api.Condition;
import net.id.incubus_core.condition.base.ConditionCommand;
import net.id.incubus_core.condition.base.ConditionManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.registry.Registry;

public class IncubusCondition implements EntityComponentInitializer {

    public static final ComponentKey<ConditionManager> CONDITION_MANAGER_KEY = ComponentRegistry.getOrCreate(IncubusCore.locate("condition_manager"), ConditionManager.class);

    public static final Registry<Condition> CONDITION_REGISTRY = FabricRegistryBuilder.createSimple(Condition.class, IncubusCore.locate("condition")).buildAndRegister();

    // Called by Cardinal Components
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, CONDITION_MANAGER_KEY, ConditionManager::new);
    }

    public static void init() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> ConditionCommand.register(dispatcher)));
    }
}
