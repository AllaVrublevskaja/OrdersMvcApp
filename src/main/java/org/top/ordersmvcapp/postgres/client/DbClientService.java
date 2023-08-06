package org.top.ordersmvcapp.postgres.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.top.ordersmvcapp.entity.Client;
import org.top.ordersmvcapp.service.ClientService;


import java.util.Optional;
@Service
@RequiredArgsConstructor
public class DbClientService implements ClientService {
    private final ClientRepository clientRepository;

    @Override
    public Client register(Client client) {
        if ((client.getId() != null && clientRepository.findById(client.getId()).isPresent()) ||
                clientRepository.findByName(client.getName()).isPresent()) {
            return null;    // клиента не добавили
        }
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> getById(Integer id) {return clientRepository.findById(id); }

    @Override
    public Iterable<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client update(Client client) {
        if (clientRepository.findById(client.getId()).isEmpty()) {
            return null;
        }
        return clientRepository.save(client);
    }
}
