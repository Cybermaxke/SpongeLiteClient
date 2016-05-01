/*
 * This file is part of LiteSpongeClient, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the Software), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED AS IS, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.client;

import com.mumfrey.liteloader.LiteMod;
import com.mumfrey.liteloader.PluginChannelListener;
import com.mumfrey.liteloader.ShutdownListener;
import org.spongepowered.client.keyboard.KeyBindingStorage;
import org.spongepowered.client.keyboard.KeyboardNetworkHandler;
import org.spongepowered.client.network.MessageChannelHandler;
import org.spongepowered.client.network.MessageRegistry;
import org.spongepowered.client.network.types.MessageKeyState;
import org.spongepowered.client.network.types.MessageKeyboardData;
import org.spongepowered.client.network.types.MessageTrackerDataRequest;
import org.spongepowered.client.network.types.MessageTrackerDataResponse;
import org.spongepowered.client.tracker.TrackerDataResponseHandler;
import net.minecraft.network.PacketBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LiteModSpongeClient implements LiteMod, ShutdownListener, PluginChannelListener {

    public static final String NAME = "LiteSpongeClient";
    public static final String VERSION = "1.0.0";

    public static final String CHANNEL_NAME = "Sponge";

    private static LiteModSpongeClient instance;

    public static LiteModSpongeClient getInstance() {
        return instance;
    }

    /**
     * The logger of this mod.
     */
    private Logger logger;

    /**
     * The handler which will process messages send between
     * client and server.
     */
    private MessageChannelHandler channelHandler;

    /**
     * The storage (save file) for custom key bindings.
     */
    private KeyBindingStorage keyBindingStorage;

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public void init(File configPath) {
        instance = this;

        this.logger = LogManager.getLogger(LiteModSpongeClient.class);

        MessageRegistry messageRegistry = new MessageRegistry();
        messageRegistry.register(0, MessageTrackerDataRequest.class);
        messageRegistry.register(1, MessageTrackerDataResponse.class, TrackerDataResponseHandler::handleTrackerResponse);
        messageRegistry.register(2, MessageKeyboardData.class, KeyboardNetworkHandler::handleKeyboardData);
        messageRegistry.register(3, MessageKeyState.class);

        this.channelHandler = new MessageChannelHandler(messageRegistry, CHANNEL_NAME, this.logger);

        this.keyBindingStorage = new KeyBindingStorage(new File("").toPath().resolve("sponge-key-bindings.txt"));
        try {
            this.keyBindingStorage.load();
        } catch (IOException e) {
            this.logger.error("An error occurred while loading the key bindings file", e);
        }
    }

    @Override
    public void onShutDown() {
        try {
            this.keyBindingStorage.save();
        } catch (IOException e) {
            this.logger.error("An error occurred while saving the key bindings file", e);
        }
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {
    }

    @Override
    public String getName() {
        return NAME;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public MessageChannelHandler getChannelHandler() {
        return this.channelHandler;
    }

    public KeyBindingStorage getKeyBindingStorage() {
        return this.keyBindingStorage;
    }

    @Override
    public void onCustomPayload(String channel, PacketBuffer data) {
        this.channelHandler.onCustomPayload(channel, data);
    }

    @Override
    public List<String> getChannels() {
        return this.channelHandler.getChannels();
    }
}
