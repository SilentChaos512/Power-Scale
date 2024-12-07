package net.silentchaos512.powerscale.config;

import com.ezylang.evalex.Expression;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.silentchaos512.powerscale.core.DifficultyUtil;
import net.silentchaos512.powerscale.evalex.ExpressionExtension;

import javax.annotation.Nullable;
import java.util.*;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ConfiguredExpression implements ExpressionExtension<ConfiguredExpression> {
    private static final Map<String, ConfiguredExpression> CONFIG_LIST = new LinkedHashMap<>();

    private final ModConfigSpec.ConfigValue<String> configValue;
    private Expression cachedExpression;

    public ConfiguredExpression(ModConfigSpec.ConfigValue<String> configValue) {
        this.configValue = configValue;
        CONFIG_LIST.put(this.getDescription(), this);
    }

    @Nullable
    public static ConfiguredExpression getFromConfigPath(String fullPath) {
        return CONFIG_LIST.getOrDefault(fullPath, null);
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        reloadConfiguredExpressions();
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        reloadConfiguredExpressions();
    }

    private static void reloadConfiguredExpressions() {
        var configuration = DifficultyUtil.getDefaultExpressionConfiguration();

        for (var configuredExpression : CONFIG_LIST.values()) {
            var expressionString = configuredExpression.configValue.get();
            configuredExpression.cachedExpression = new Expression(expressionString, configuration);
        }
    }

    @Override
    public String getDescription() {
        return String.join(".", this.configValue.getPath());
    }

    @Override
    public Expression expression() {
        return this.cachedExpression;
    }

    public String getConfigDefault() {
        return this.configValue.getDefault();
    }

    @Override
    public void checkExpressionNotNull() {
        if (cachedExpression == null) {
            throw new NullPointerException("expression config value not loaded? {}" + configValue.getPath());
        }
    }
}
