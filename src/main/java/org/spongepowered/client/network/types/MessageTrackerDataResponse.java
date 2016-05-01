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
package org.spongepowered.client.network.types;

import org.spongepowered.client.network.Message;
import net.minecraft.network.PacketBuffer;

import java.io.IOException;

import javax.annotation.Nullable;

public final class MessageTrackerDataResponse implements Message {

    @Nullable private String owner;
    @Nullable private String notifier;

    public MessageTrackerDataResponse() {
    }

    @Override
    public void writeTo(PacketBuffer buf) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readFrom(PacketBuffer buf) throws IOException {
        int ownerLength = buf.readInt();
        this.owner = new String(buf.readBytes(ownerLength).array());
        if (this.owner.isEmpty()) {
            this.owner = null;
        }
        int notifierLength = buf.readInt();
        this.notifier = new String(buf.readBytes(notifierLength).array());
        if (this.notifier.isEmpty()) {
            this.notifier = null;
        }
    }

    @Nullable
    public String getOwner() {
        return this.owner;
    }

    @Nullable
    public String getNotifier() {
        return this.notifier;
    }
}
