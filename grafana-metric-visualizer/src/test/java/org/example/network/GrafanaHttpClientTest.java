package org.example.network;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrafanaHttpClientTest {

    @Test
    void sendDashboard() throws Exception {
        String json = "{\"title\":\"CPU\"}";

        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        try (var httpClientMock = mockStatic(HttpClient.class)) {
            httpClientMock.when(HttpClient::newHttpClient).thenReturn(mockClient);

            assertDoesNotThrow(() -> GrafanaHttpClient.sendDashboard(json));

            verify(mockClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }
    }


    @Test
    void sendDashboard_WhenHttpClientFails_ThrowsIOException() throws Exception {
        String json = "{\"title\":\"Error\"}";

        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.send(any(), any())).thenThrow(new IOException("Connection refused"));

        try (var httpClientMock = mockStatic(HttpClient.class)) {
            httpClientMock.when(HttpClient::newHttpClient).thenReturn(mockClient);
            assertThrows(IOException.class, () -> GrafanaHttpClient.sendDashboard(json));
        }
    }

    @Test
    void sendDashboard_WhenInterrupted_ThrowsInterruptedException() throws Exception {
        String json = "{\"title\":\"Interrupted\"}";

        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.send(any(), any())).thenThrow(new InterruptedException("Test interrupted"));

        try (var httpClientMock = mockStatic(HttpClient.class)) {
            httpClientMock.when(HttpClient::newHttpClient).thenReturn(mockClient);
            assertThrows(InterruptedException.class, () -> GrafanaHttpClient.sendDashboard(json));
        }
    }
}
