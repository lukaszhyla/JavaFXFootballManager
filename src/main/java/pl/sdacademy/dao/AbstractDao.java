package pl.sdacademy.dao;

import pl.sdacademy.entity.Entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class AbstractDao<T extends Entity> {
	protected List<T> entities;

	public AbstractDao() {
		entities = new ArrayList<>();

		try {
			Path filePath = Paths.get(getFileName());
			if(!Files.exists(filePath)) {
				Files.createFile(filePath);
			}
			Stream<String> linesStream = Files.lines(filePath);
			linesStream.forEach(l -> entities.add(createEntityFromFileLine(l)));
			
//			List<String> fileLines = Files.readAllLines(Paths.get(getFileName()));
//			for (String fileLine : fileLines) {
//				entities.add(createEntityFromFileLine(fileLine));
//			}
			
			linesStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Bład odczytu encji"
					+ " z pliku " + getFileName());
		}
	}

	public T get(int id) {
		for (T entity : entities) {
			if(entity.getId() == id) {
				return copy(entity);
			}
		}
		return null;
//		return products.stream()
//				.filter(p -> p.getId() == id)
//				.map(p -> new Product(p))
//				.findFirst().orElse(null);
	}

	public List<T> getAll() {
		return entities.stream()
				.map(e -> copy(e))
				.collect(Collectors.toList());
	}

	public void deleteAll() {
		entities.clear();
		saveToFile();
	}

	public void delete(int id) {
		for(T entity: entities) {
			if(entity.getId() == id) {
				entities.remove(entity);
				saveToFile();
				return;
			}
		}
		
//		products = products.stream()
//				.filter(p -> p.getId() != id)
//				.collect(Collectors.toList());
//		saveToFile();
	}

	protected void saveToFile() {
		List<String> fileLines = new ArrayList<>();
		for (T entity : entities) {
			fileLines.add(createFileLineFromEntity(entity));
		}

//		List<String> fileLines = products.stream()
//				.map(p -> createFileLineFromProduct(p))
//				.collect(Collectors.toList());
		
		try {
			Files.write(Paths.get(getFileName()), 
					fileLines, 
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("Błąd podczas zapisu pliku " + getFileName());
		}
	}

	public void add(T entity) {
		entity.setId(getNextId());
		entities.add(copy(entity));
		saveToFile();
	}

	public void update(T entity) {
		for(T e: entities) {
			if(e.getId() == entity.getId()) {
				update(e, entity);
				saveToFile();
			}
		}
		
//		products.stream()
//			.filter(p -> p.getId() == product.getId())
//			.forEach(p -> {
//				p.setName(product.getName());
//				p.setValue(product.getValue());
//				saveToFile();
//			});
	}

	private int getNextId() {
		int maxId = 0;
		for (T entity : entities) {
			if(entity.getId() > maxId) {
				maxId = entity.getId();
			}
		}
		return maxId + 1;
		
//		return entities.stream()
//				.mapToInt(e -> e.getId())
//				.max()
//				.orElse(0) + 1;
	}
	
	protected abstract void update(T entity, T entityWithNewValues);
	
	protected abstract String createFileLineFromEntity(T entity);

	protected abstract T copy(T entity);

	protected abstract String getFileName();

	protected abstract T createEntityFromFileLine(String fileLine);
}
