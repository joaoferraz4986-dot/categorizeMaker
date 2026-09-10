package com.makernav.categorize.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.makernav.categorize.dto.ProjetoRequestDTO;
import com.makernav.categorize.dto.ProjetoResponseDTO;
import com.makernav.categorize.infra.repository.ProjetoRepository;
import com.makernav.categorize.model.Projeto;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;

    public ProjetoService(ProjetoRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }

    public List<ProjetoResponseDTO> listarTodos() {
        return projetoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<ProjetoResponseDTO> buscarPorId(int id) {
        return projetoRepository.findById(id).map(this::toResponse);
    }

    public ProjetoResponseDTO salvar(ProjetoRequestDTO dto) {
        Projeto projeto = new Projeto();
        projeto.setNome(dto.nome());
        projeto.setCategoria(dto.categoria());
        projeto.setDescricao(dto.descricao());
        return toResponse(projetoRepository.save(projeto));
    }

    public void deletar(int id) {
        projetoRepository.deleteById(id);
    }

    private ProjetoResponseDTO toResponse(Projeto projeto) {
        return new ProjetoResponseDTO(projeto.getNome(), projeto.getCategoria(), projeto.getDescricao());
    }
}
