package com.senai.chamados.services;

import java.util.ArrayList;
import java.util.List;

import com.senai.chamados.dtos.ChamadoDto;
import com.senai.chamados.models.ChamadoEntity;
import com.senai.chamados.repositories.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.Set;

@Service
public class ChamadoService {

    private static final Set<String> STATUS_VALIDOS =
            Set.of("ABERTO", "EM_ANDAMENTO", "CONCLUIDO");

    private final ChamadoRepository repository;

    public ChamadoService(ChamadoRepository repository) {
        this.repository = repository;
    }

    public List<ChamadoDto> obter() {
        List<ChamadoDto> lista = new ArrayList<>();
        for (ChamadoEntity entity : repository.findAll()) {
            lista.add(new ChamadoDto(
                    entity.getId(),
                    entity.getTitulo(),
                    entity.getDescricao(),
                    entity.getStatus()
            ));
        }
        return lista;
    }

    public ChamadoDto buscar(Long id) {
        Optional<ChamadoEntity> entity = repository.findById(id);
        if (entity.isPresent()) {
            ChamadoEntity e = entity.get();
            return new ChamadoDto(e.getId(), e.getTitulo(), e.getDescricao(), e.getStatus());
        }
        return null;
    }

    public boolean cadastrar(ChamadoDto dto) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()
                || dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            return false;
        }
        ChamadoEntity entity = new ChamadoEntity();
        entity.setTitulo(dto.getTitulo());
        entity.setDescricao(dto.getDescricao());
        entity.setStatus("ABERTO");
        repository.save(entity);
        return true;
    }

    public boolean atualizar(Long id, ChamadoDto dto) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()
                || dto.getDescricao() == null || dto.getDescricao().isBlank()
                || !STATUS_VALIDOS.contains(dto.getStatus())) {
            return false;
        }
        Optional<ChamadoEntity> entity = repository.findById(id);
        if (entity.isEmpty()) {
            return false;
        }
        ChamadoEntity e = entity.get();
        e.setTitulo(dto.getTitulo());
        e.setDescricao(dto.getDescricao());
        e.setStatus(dto.getStatus());
        repository.save(e);
        return true;
    }

    public boolean excluir(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

}
