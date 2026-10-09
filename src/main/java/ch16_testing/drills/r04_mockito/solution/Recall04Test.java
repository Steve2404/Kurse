package ch16_testing.drills.r04_mockito.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/** Le corrige du drill 4 : l'API de Mockito. */
@ExtendWith(MockitoExtension.class)
class Recall04Test {

    interface Thermometer {
        int celsius();
    }

    interface Display {
        void show(String text);

        void clear();
    }

    interface Converter {
        String convert(String input);
    }

    @Mock
    Display display;

    @Test
    void d01() {
        Thermometer t = mock(Thermometer.class);
        when(t.celsius()).thenReturn(21);
        assertEquals(21, t.celsius());
    }

    @Test
    void d02() {
        assertAll(
                () -> assertNull(mock(Converter.class).convert("x")),
                () -> assertEquals(0, mock(Thermometer.class).celsius()));
    }

    @Test
    void d03() {
        Thermometer t = mock(Thermometer.class);
        when(t.celsius()).thenThrow(new IllegalStateException("panne"));
        IllegalStateException e = assertThrows(IllegalStateException.class, t::celsius);
        assertEquals("panne", e.getMessage());
    }

    // Piege : la DERNIERE reponse se repete pour tous les appels suivants.
    @Test
    void d04() {
        Thermometer t = mock(Thermometer.class);
        when(t.celsius()).thenReturn(1, 2, 3);
        assertEquals(List.of(1, 2, 3, 3), List.of(t.celsius(), t.celsius(), t.celsius(), t.celsius()));
    }

    @Test
    void d05() {
        display.show("a");
        display.show("a");
        verify(display, times(2)).show("a");
        verify(display, never()).clear();
        verify(display, atLeastOnce()).show(anyString());
    }

    @Test
    void d06() {
        display.show("bonjour");
        display.show("au revoir");
        ArgumentCaptor<String> text = ArgumentCaptor.forClass(String.class);
        verify(display, times(2)).show(text.capture());
        assertAll(
                () -> assertEquals("au revoir", text.getValue()),
                () -> assertEquals(List.of("bonjour", "au revoir"), text.getAllValues()));
    }

    @Test
    void d07() {
        display.clear();
        display.show("x");
        InOrder order = inOrder(display);
        order.verify(display).clear();
        order.verify(display).show("x");
    }

    @Test
    void d08() {
        Converter c = mock(Converter.class);
        when(c.convert(anyString())).thenAnswer(invocation -> invocation.getArgument(0, String.class).toUpperCase());
        assertEquals("ABC", c.convert("abc"));
    }

    // Piege : sur un espion, when(spy.size()) appellerait la VRAIE methode ; doReturn(...).when(spy) ne l'appelle pas.
    @Test
    void d09() {
        List<String> list = spy(new ArrayList<>());
        list.add("a");
        verify(list).add("a");
        assertEquals(1, list.size());
        doReturn(100).when(list).size();
        assertEquals(100, list.size());
    }

    // Une methode void ne peut pas aller dans when(...) : doThrow(...).when(mock).methode().
    @Test
    void d10() {
        doThrow(new IllegalStateException("ecran casse")).when(display).clear();
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> display.clear());
        assertEquals("ecran casse", e.getMessage());
    }

    @Test
    void d11() {
        Display unused = mock(Display.class);
        verifyNoInteractions(unused);
        display.show("seul appel");
        verify(display).show("seul appel");
        verifyNoMoreInteractions(display);
    }

    @Test
    void d12() {
        Converter c = mock(Converter.class);
        when(c.convert(eq("a"))).thenReturn("A");
        when(c.convert(argThat(s -> s.startsWith("x")))).thenReturn("X");
        assertAll(
                () -> assertEquals("A", c.convert("a")),
                () -> assertEquals("X", c.convert("xyz")));
    }
}
