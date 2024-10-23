package com.miniplm.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.miniplm.entity.ChangeTypeEnum;
import com.miniplm.entity.Filedata;
import com.miniplm.entity.Form;
import com.miniplm.entity.FormHistory;
import com.miniplm.entity.YmlData;
import com.miniplm.entity.ZAccount;
import com.miniplm.exception.BusinessException;
import com.miniplm.repository.FiledataRepository;
import com.miniplm.repository.FormHistoryRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.response.FiledataResponse;
import com.miniplm.utils.UUIDTools;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service("FileStorageService")
@Slf4j
public class FileStorageService {
	@Autowired
	private YmlData ymlData;
	
//	@Autowired
//	private UserRepository userRepository;
	
    private Path path;
	
	@Autowired
	private FiledataRepository filedataRepository;
	
	@Autowired
	private FormHistoryRepository formHistoryRepository;
	
	@Autowired
	private FormRepository formRepository;
	
	@SneakyThrows
	private void verifyFile(MultipartFile file) {
//		Long fileSize = file.getSize();
//		userRepository
		
		if (file.isEmpty()) {
			throw new BusinessException("File is empty");
		}
	}
	
	public void init() {
		try {
			path = Paths.get(ymlData.getFileStorage());
			log.info("Storage Folder Path: {}", path);
//			System.out.println("Storage Folder Path:"+path);
			if (Files.notExists(path)) {
				Files.createDirectory(path);
				log.info("Storage Folder Init success!");
//				System.out.println("Storage Folder Init success!");
			}else {
				log.info("Storage Folder exist!");
//				System.out.println("Storage Folder exist!");				
			}

		} catch (IOException e) {
			throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Transactional
	public FiledataResponse save(MultipartFile multipartFile) {
		try {
			Filedata fileData = new Filedata();
			String uuid = UUIDTools.getUUID();
			long size = multipartFile.getSize();
			String uploadDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			Path subPath = path.resolve(uploadDate);
			if (Files.notExists(subPath)) {
				Files.createDirectory(subPath);
				log.info("Storage Sub Folder create success!");
//				System.out.println("Storage Sub Folder create success!");
			}else {
				log.info("Storage Sub Folder exist!");
//				System.out.println("Storage Sub Folder exist!");				
			}
			InputStream io = multipartFile.getInputStream();
			Files.copy(io, subPath.resolve(uuid));
			io.close();
			fileData.setStorageUuid(uuid);
			fileData.setStorageFolder(uploadDate);
			fileData.setFileSize(size);
			fileData.setFileName(multipartFile.getOriginalFilename());
			Filedata savedFiledata = filedataRepository.save(fileData);
			log.info("File Name: {}", savedFiledata.getFileName());
//			System.out.println("File Name:"+savedFiledata.getFileName());
			return new FiledataResponse(savedFiledata);
		} catch (IOException e) {
			throw new RuntimeException("Could not store the file. Error:" + e.getMessage());
		}

	}
	
	@Transactional
	public FiledataResponse save(String formId, MultipartFile multipartFile) {
		try {
			Form form = formRepository.getReferenceById(Long.valueOf(formId));
			Filedata fileData = new Filedata();
			String uuid = UUIDTools.getUUID();
			long size = multipartFile.getSize();
			String uploadDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			Path subPath = path.resolve(uploadDate);
			if (Files.notExists(subPath)) {
				Files.createDirectory(subPath);
				log.info("Storage Sub Folder create success!");
//				System.out.println("Storage Sub Folder create success!");
			}else {
				log.info("Storage Sub Folder exist!");
//				System.out.println("Storage Sub Folder exist!");				
			}
			InputStream io = multipartFile.getInputStream();
			Files.copy(io, subPath.resolve(uuid));
			io.close();
			fileData.setStorageUuid(uuid);
			fileData.setStorageFolder(uploadDate);
			fileData.setFileSize(size);
			fileData.setFileName(multipartFile.getOriginalFilename());
			fileData.setForm(form);
			Filedata savedFiledata = filedataRepository.save(fileData);
			log.info("File Name: {}" ,savedFiledata.getFileName());
//			System.out.println("File Name:"+savedFiledata.getFileName());
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    	ZAccount user = null;
			if (authentication != null && authentication.isAuthenticated()) {
				user = (ZAccount) authentication.getPrincipal();
			}
			
			FormHistory history = FormHistory.builder()
	    			.detail("Upload file: "+ savedFiledata.getFileName())
	    			.form(form)
	    			.accountName(user.getUsername())
	    			.stepName(form.getCurrStep().getStepName())
	    			.type(ChangeTypeEnum.UploadFiles)
	    			.build();
	    	formHistoryRepository.save(history);
			
			return new FiledataResponse(savedFiledata);
		} catch (IOException e) {
			throw new RuntimeException("Could not store the file. Error:" + e.getMessage());
		}

	}
	
	@Transactional
	public FiledataResponse save(String formId,String dataIndex, MultipartFile multipartFile) {
		try {
			Form form = formRepository.getReferenceById(Long.valueOf(formId));
			Filedata fileData = new Filedata();
			String uuid = UUIDTools.getUUID();
			long size = multipartFile.getSize();
			String uploadDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			Path subPath = path.resolve(uploadDate);
			if (Files.notExists(subPath)) {
				Files.createDirectory(subPath);
				log.info("Storage Sub Folder create success!");
//				System.out.println("Storage Sub Folder create success!");
			}else {
				log.info("Storage Sub Folder exist!");
//				System.out.println("Storage Sub Folder exist!");				
			}
			InputStream io = multipartFile.getInputStream();
			Files.copy(io, subPath.resolve(uuid));
			io.close();
			fileData.setStorageUuid(uuid);
			fileData.setStorageFolder(uploadDate);
			fileData.setFileSize(size);
			fileData.setFileName(multipartFile.getOriginalFilename());
			fileData.setForm(form);
			fileData.setDataIndex(dataIndex);
			Filedata savedFiledata = filedataRepository.save(fileData);
			log.info("File Name: {}", savedFiledata.getFileName());
//			System.out.println("File Name:"+savedFiledata.getFileName());
			
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    	ZAccount user = null;
			if (authentication != null && authentication.isAuthenticated()) {
				user = (ZAccount) authentication.getPrincipal();
			}
			
			FormHistory history = FormHistory.builder()
	    			.detail("Upload file: "+ savedFiledata.getFileName())
	    			.form(form)
	    			.accountName(user.getUsername())
	    			.stepName(form.getCurrStep().getStepName())
	    			.type(ChangeTypeEnum.UploadFiles)
	    			.build();
	    	formHistoryRepository.save(history);
			return new FiledataResponse(savedFiledata);
		} catch (IOException e) {
			throw new RuntimeException("Could not store the file. Error:" + e.getMessage());
		}

	}
	
	@Transactional
	public void deleteFile(String uuid) {
		Filedata fd = filedataRepository.findByStorageUuid(uuid);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	ZAccount user = null;
		if (authentication != null && authentication.isAuthenticated()) {
			user = (ZAccount) authentication.getPrincipal();
		}
		
		FormHistory history = FormHistory.builder()
    			.detail("Delete file: "+ fd.getFileName())
    			.form(fd.getForm())
    			.accountName(user.getUsername())
    			.stepName(fd.getForm().getCurrStep().getStepName())
    			.type(ChangeTypeEnum.DeleteFiles)
    			.build();
    	formHistoryRepository.save(history);
		
		filedataRepository.deleteByStorageUuid(uuid);
	}
	
	@Transactional
	public void deleteById(Long id) {
//		Filedata fd = filedataRepository.findByStorageUuid(uuid);
		Filedata fd = filedataRepository.getReferenceById(id);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	ZAccount user = null;
		if (authentication != null && authentication.isAuthenticated()) {
			user = (ZAccount) authentication.getPrincipal();
		}
		
		FormHistory history = FormHistory.builder()
    			.detail("Delete file: "+ fd.getFileName())
    			.form(fd.getForm())
    			.accountName(user.getUsername())
    			.stepName(fd.getForm().getCurrStep().getStepName())
    			.type(ChangeTypeEnum.DeleteFiles)
    			.build();
    	formHistoryRepository.save(history);
		
		filedataRepository.deleteById(id);
	}

	public Resource load(String uuid) {
		Filedata fd = filedataRepository.findByStorageUuid(uuid);
		Path file = path.resolve(fd.getStorageFolder()+"/"+uuid);
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    	ZAccount user = null;
//		if (authentication != null && authentication.isAuthenticated()) {
//			user = (ZAccount) authentication.getPrincipal();
//		}
		try {
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
//				FormHistory history = FormHistory.builder()
//		    			.detail("Get file: "+ fd.getFileName())
//		    			.form(fd.getForm())
//		    			.accountName((String)authentication.getPrincipal())
//		    			.stepName(fd.getForm().getCurrStep().getStepName())
//		    			.type(ChangeTypeEnum.GetFiles)
//		    			.build();
//		    	formHistoryRepository.save(history);
				
				return resource;
			} else {
				throw new RuntimeException("Could not read the file.");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error:" + e.getMessage());
		}
	}

	public Stream<Path> load() {
		try {
			return Files.walk(this.path, 1).filter(path -> !path.equals(this.path)).map(this.path::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files.");
		}
	}

	public void clear() {
		FileSystemUtils.deleteRecursively(path.toFile());
	}

}
