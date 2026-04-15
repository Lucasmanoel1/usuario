package com.lucasmanoel.usuario.business;

import com.lucasmanoel.usuario.infrastructure.clients.ViaCepClient;
import com.lucasmanoel.usuario.infrastructure.clients.ViaCepDTO;
import com.lucasmanoel.usuario.infrastructure.exceptions.IllegalArgumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final ViaCepClient client;

    public ViaCepDTO buscaDadosEndereco(String cep) {
        return client.buscaDadosEndereco(processaCep(cep));

    }

    public String processaCep(String cep) {
        if (cep == null) {
            throw new IllegalArgumentException("Cep inválido");
        }

        String cepFormatado = cep.replaceAll("[^0-9]", "");

        if (cepFormatado.length() != 8) {
            throw new IllegalArgumentException("Cep inválido");
        }
        return cepFormatado;
    }
}
