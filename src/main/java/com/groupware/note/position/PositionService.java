package com.groupware.note.position;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.groupware.note.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PositionService {
	private final PositionRepository positionRepository;

	public Positions findById(Integer id) {
		Optional<Positions> _position = this.positionRepository.findById(id);
		if(_position.isEmpty()) {
			throw new DataNotFoundException("");
		}
		return _position.get();
	}
}
