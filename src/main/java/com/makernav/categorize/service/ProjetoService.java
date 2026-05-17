package com.makernav.categorize.service;

import com.makernav.categorize.dto.ProjetoRequestDTO;
import com.makernav.categorize.dto.ProjetoResponseDTO;
import com.makernav.categorize.infra.repository.ProjetoRepository;
import com.makernav.categorize.model.Projeto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    public List<ProjetoResponseDTO> listarTodos() {
        return projetoRepository.findAll()
                .stream()
                .map(p -> new ProjetoResponseDTO(p.getNome(), p.getCategoria(), p.getDescricao()))
                .toList();
    }


    public Optional<ProjetoResponseDTO> buscarPorId(int id) {
        return projetoRepository.findById(id)
                .map(p -> new ProjetoResponseDTO(p.getNome(), p.getCategoria(), p.getDescricao()));
    }
    public ProjetoResponseDTO salvar(ProjetoRequestDTO dto) {

        Projeto projeto = new Projeto();
        projeto.setNome(dto.nome());
        projeto.setCategoria(dto.categoria());
        projeto.setDescricao(dto.descricao());

        Projeto salvo = projetoRepository.save(projeto);

        return new ProjetoResponseDTO(salvo.getNome(), salvo.getCategoria(), salvo.getDescricao());
    }

    public void deletar(int id) {
        projetoRepository.deleteById(id);
    }
}
