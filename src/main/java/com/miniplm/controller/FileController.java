package com.miniplm.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.miniplm.response.MessageResponse;
import com.miniplm.entity.Filedata;
import com.miniplm.entity.Form;
import com.miniplm.repository.FiledataRepository;
import com.miniplm.repository.FormRepository;
import com.miniplm.response.FiledataResponse;
import com.miniplm.service.FileStorageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "FileUpload", description = "檔案上下傳相關的API")
@RestController
@RequestMapping("/api/v1/files")
@CrossOrigin
@Slf4j
public class FileController {
	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private FiledataRepository filedataRepository;
	
	@Autowired
	private FormRepository formRepository;

//	public FileUploadController(FileStorageService fileStorageService, FiledataRepository filedataRepository) {
//		this.fileStorageService = fileStorageService;
//		this.filedataRepository = filedataRepository;
//	}

	@PostMapping(name ="不指定物件的上傳檔案", 
			     consumes = "multipart/*" , 
			     headers = "content-type=multipart/form-data")
	public ResponseEntity<FiledataResponse> upload(@RequestParam("file") MultipartFile file) {
		log.info("上傳1");
		try {
			return ResponseEntity.ok(fileStorageService.save(file));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping(value = "/forms/{formId}", consumes = "multipart/*" , headers = "content-type=multipart/form-data", name = "依表單物件上傳檔案")
	public ResponseEntity<FiledataResponse> upload(@PathVariable("formId")String formId, @RequestParam("file") MultipartFile file) {
		log.info("上傳2");
//		System.out.println("上傳2");
		try {
//			fileStorageService.save(formId, file);
			return ResponseEntity.ok(fileStorageService.save(formId, file));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping(value = "/forms/{formId}/{dataIndex}", consumes = "multipart/*" , headers = "content-type=multipart/form-data",  name = "依欄位上傳檔案" )
	public ResponseEntity<FiledataResponse> upload(@PathVariable("formId")String formId, @PathVariable("dataIndex")String dataIndex, @RequestParam("file") MultipartFile file) {
		log.info("上傳3");
//		System.out.println("上傳3");
		try {
//			fileStorageService.save(formId, file);
			return ResponseEntity.ok(fileStorageService.save(formId,dataIndex,file));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping(value="/forms/{formId}", name="依表單取得檔案清單")
    public ResponseEntity<List<FiledataResponse>> formFiles(@PathVariable("formId")String formId){  
		Form form = formRepository.getReferenceById(Long.valueOf(formId));
		List<Filedata> filedatas= filedataRepository.findByForm(form);
		List<FiledataResponse> files = new ArrayList<>();
				
		filedatas.forEach(filedata -> {
			files.add(new FiledataResponse(filedata)); 
		});
		
//        List<FiledataResponse> files = fileStorageService.load()  
//                .map(path -> {  
//                    String fileName = path.getFileName().toString();  
//                    String url = MvcUriComponentsBuilder  
//                            .fromMethodName(FileUploadController.class,  
//                                    "getFile",  
//                                    path.getFileName().toString()  
//                            ).build().toString();  
//                    return new FiledataResponse(fileName,"1234", 123L,url);
//                }).collect(Collectors.toList());  
        return ResponseEntity.ok(files);  
    }  
	
	@GetMapping(value = "/forms/{formId}/{dataIndex}", name="依表單欄位取得檔案清單")
    public ResponseEntity<List<FiledataResponse>> fieldFiles(@PathVariable("formId")String formId, @PathVariable("dataIndex")String dataIndex ){  
		Form form = formRepository.getReferenceById(Long.valueOf(formId));
		List<Filedata> filedatas= filedataRepository.findByFormAndDataIndex(form, dataIndex);
		List<FiledataResponse> files = new ArrayList<>();
				
		filedatas.forEach(filedata -> {
			files.add(new FiledataResponse(filedata)); 
		});
		
//        List<FiledataResponse> files = fileStorageService.load()  
//                .map(path -> {  
//                    String fileName = path.getFileName().toString();  
//                    String url = MvcUriComponentsBuilder  
//                            .fromMethodName(FileUploadController.class,  
//                                    "getFile",  
//                                    path.getFileName().toString()  
//                            ).build().toString();  
//                    return new FiledataResponse(fileName,"1234", 123L,url);
//                }).collect(Collectors.toList());  
        return ResponseEntity.ok(files);  
    }  

	@GetMapping(name="取得所有檔案清單")
    public ResponseEntity<List<FiledataResponse>> files(){  
		List<Filedata> filedatas= filedataRepository.findAll();
		List<FiledataResponse> files = new ArrayList<>();
				
		filedatas.forEach(filedata -> {
			files.add(new FiledataResponse(filedata)); 
		});
		
//        List<FiledataResponse> files = fileStorageService.load()  
//                .map(path -> {  
//                    String fileName = path.getFileName().toString();  
//                    String url = MvcUriComponentsBuilder  
//                            .fromMethodName(FileUploadController.class,  
//                                    "getFile",  
//                                    path.getFileName().toString()  
//                            ).build().toString();  
//                    return new FiledataResponse(fileName,"1234", 123L,url);
//                }).collect(Collectors.toList());  
        return ResponseEntity.ok(files);  
    }  
  
//    @GetMapping("/{filename:.+}") 
//    public ResponseEntity<Resource> getFile(@PathVariable("filename")String filename){  
//        Resource file = fileStorageService.load(filename);  
//        return ResponseEntity.ok()  
//                .header(HttpHeaders.CONTENT_DISPOSITION,  
//                        "attachment;filename=\""+file.getFilename()+"\"")  
//                .body(file);  
//    }
    
    @GetMapping(value="/uuid/{uuid}", name="依uuid取得檔案") 
    public ResponseEntity<Resource> getFile(@PathVariable("uuid")String uuid) throws UnsupportedEncodingException{  
        Resource file = fileStorageService.load(uuid);  
        Filedata fd = filedataRepository.findByStorageUuid(uuid);
        String filename = URLEncoder.encode(fd.getFileName(),"utf-8");
        return ResponseEntity.ok()  
                .header(HttpHeaders.CONTENT_DISPOSITION,  
                        "attachment; filename=\""+filename+"\"") 
                .header(HttpHeaders.CONTENT_TYPE, "multipart/form-data")
                .body(file);  
    }  
    
    @DeleteMapping(value="/uuid/{uuid}", name="依uuid刪除檔案")
    public ResponseEntity deleteFile(@PathVariable("uuid")String uuid) {
    	fileStorageService.deleteFile(uuid);
    	return ResponseEntity.noContent().build();
    }
}
