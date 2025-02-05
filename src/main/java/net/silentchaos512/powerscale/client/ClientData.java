package net.silentchaos512.powerscale.client;

import net.silentchaos512.powerscale.network.payload.ClientDataUpdatePayload;

public record ClientData(
        float playerDifficulty,
        float localDifficulty
) {
    private static ClientData data = new ClientData(0f, 0f);

    public static ClientData get() {
        return data;
    }

    public static void update(ClientDataUpdatePayload payload) {
        data = new ClientData(payload.playerDifficulty(), payload.localDifficulty());
    }
}
