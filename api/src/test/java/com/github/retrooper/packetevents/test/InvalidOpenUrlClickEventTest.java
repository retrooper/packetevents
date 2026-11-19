/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2026 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InvalidOpenUrlClickEventTest extends BaseDummyAPITest {

    @Test
    @DisplayName("open_url click event with a value that is not a valid URI does not fail parsing")
    public void testInvalidOpenUrlDoesNotFailParsing() {
        String url = "http://as.link}";
        String json = "{\"text\":\"" + url + "\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"" + url + "\"}}";

        Component component = assertDoesNotThrow(() -> AdventureSerializer.serializer(ClientVersion.V_1_12_2).fromJson(json));

        assertInstanceOf(TextComponent.class, component);
        assertEquals(url, ((TextComponent) component).content());

        // Adventure 5 validates open_url values with java.net.URI and rejects this one, so the
        // click event is dropped. Older Adventure versions accept any string and keep it.
        if (adventureRejectsOpenUrl(url)) {
            assertNull(component.clickEvent());
        } else {
            assertEquals(ClickEvent.openUrl(url), component.clickEvent());
        }
    }

    private static boolean adventureRejectsOpenUrl(String url) {
        try {
            ClickEvent.openUrl(url);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    @Test
    @DisplayName("open_url click event with a valid URL is kept")
    public void testValidOpenUrlIsKept() {
        String json = "{\"text\":\"link\",\"clickEvent\":{\"action\":\"open_url\",\"value\":\"https://example.com/page\"}}";

        Component component = AdventureSerializer.serializer(ClientVersion.V_1_12_2).fromJson(json);

        assertEquals(ClickEvent.openUrl("https://example.com/page"), component.clickEvent());
    }
}
