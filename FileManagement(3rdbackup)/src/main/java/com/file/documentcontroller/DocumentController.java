package com.file.documentcontroller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.file.entity.Document;
import com.file.repo.DocumentRepository;
import com.file.services.DocumentService;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")

public class DocumentController 
{
	 @Autowired
     private DocumentRepository documentRepository;
	 
	private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

	private final DocumentService documentService;
	
	public DocumentController(DocumentService documentService) 
	{
		this.documentService = documentService;
	}

//	@PostMapping("/upload")
//
//	public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file,
//
//			@RequestParam("author") String author) {
//
//		try {
//			documentService.saveFile(file, author);
//
//			Map<String, String> response = new HashMap<>();
//			response.put("message", "File uploaded successfully!");
//
//			return ResponseEntity.ok(response);
//
//		} catch (Exception e) {
//
//			Map<String, String> errorResponse = new HashMap<>();
//
//			errorResponse.put("message", "Upload failed: " + e.getMessage());
//
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//
//		}
//
//	}

	
	/*@PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("authorId") String authorId) {
        System.out.println("Received authorId: " + authorId); // Debugging
 
        try {
            Document document = documentService.saveFile(file, authorId);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }*/
	
	@PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("authorName") String authorName) { // Changed authorId -> authorName
 
        try {
            Document document = documentService.saveFile(file, authorName);
            Map<String, String> response = new HashMap<>();
            response.put("message", "File uploaded successfully!");
            response.put("fileName", document.getFileName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }
	
	@GetMapping("/documents/decrypt/{fileName}")
	public ResponseEntity<byte[]> viewDocument(@PathVariable String fileName) {
	    try {
	        byte[] decryptedFile = documentService.decryptFile(fileName);
	        return ResponseEntity.ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName)
	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
	                .body(decryptedFile);
	    } catch (Exception e) {
	    	 log.error("Error decrypting file: {}", e.getMessage(), e);
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                 .body(new byte[0]); 
	       }
	}
	
//	@GetMapping("/documents/decrypt/{fileId}")
//	public ResponseEntity<?> decryptFile(@PathVariable String fileId) {
//	    try {
//	        byte[] decryptedFile = documentService.decryptFile(fileId);
//	        return ResponseEntity.ok()
//	                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//	                .body(decryptedFile);
//	    } catch (Exception e) {
//	        log.error("Error decrypting file: {}", e.getMessage());
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body("File decryption failed");
//	    }
//	}


	
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
		try {
			Optional<Document> documentOpt = documentService.getFile(id);
			if (documentOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}

			String fileName = documentOpt.get().getFileName();
			byte[] fileContent = documentService.decryptFile(fileName);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(fileContent);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(("Error: " + e.getMessage()).getBytes());
		}
	}
	
	 //  Fetch all documents (Angular Dashboard)
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    //  Fetch document by ID
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return documentService.getFile(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //  Update document
//    @PutMapping("/{id}")
//    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document updatedDocument) {
//        Document updated = documentService.updateDocument(id, updatedDocument);
//        return ResponseEntity.ok(updated);
//    }
//    @PutMapping("/{id}")
//    public ResponseEntity<Document> updateDocument(@PathVariable Long id, @RequestBody Document updatedDocument) {
//        Document updated = documentService.updateDocument(
//            id,
//            updatedDocument.getAuthor(),
//            updatedDocument.getUploadedAt(),
//            null // Since `@RequestBody` does not handle file upload, pass null for now
//        );
//        return ResponseEntity.ok(updated);
//    }
//    
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateDocument(
//            @PathVariable Long id,
//            @RequestParam("authorName") String authorName,
//            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//        
//       
//		Optional<Document> existingDoc = documentRepository.findById(id);
//        if (!existingDoc.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Document not found");
//        }
//
//        Document document = existingDoc.get();
//        document.setAuthor(authorName);
//        document.setUploadedAt(LocalDateTime.now()); // Update timestamp
//
//        if (file != null && !file.isEmpty()) {
//            String fileName = file.getOriginalFilename();
//            document.setFileName(fileName);
//            document.setFileType(file.getContentType());
//
//            // Save the file (Modify based on your storage logic)
//            Path path = Paths.get("D:/uploads/" + fileName);
//            Files.write(path, file.getBytes());
//        }
//
//        documentRepository.save(document);
//        return ResponseEntity.ok("Document updated successfully");
//    }
    
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDocument(
            @PathVariable Long id,
            @RequestParam("authorName") String authorName,
            @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        Optional<Document> existingDoc = documentRepository.findById(id);
        if (!existingDoc.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Document not found"));
        }

        Document document = existingDoc.get();
        document.setAuthor(authorName);
        document.setUploadedAt(LocalDateTime.now()); // Update timestamp

        if (file != null && !file.isEmpty()) {
            // **Delete old file before saving the new one**
            Path oldFilePath = Paths.get("D:/Documents/enc_" + document.getFileName());
            Files.deleteIfExists(oldFilePath);

            // **Generate random name for new file**
            String randomFileName = UUID.randomUUID().toString();

            // **Encrypt and save file**
            byte[] encryptedContent = documentService.encryptFile(file.getBytes());
            Path newFilePath = Paths.get("D:/Documents/enc_" + randomFileName);
            Files.write(newFilePath, encryptedContent);

            // **Update document entity**
            document.setFileName(randomFileName);
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
        }

        documentRepository.save(document);
        return ResponseEntity.ok(Map.of("message", "Document updated successfully", "fileName", document.getFileName()));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateDocument(
//            @PathVariable Long id,
//            @RequestParam("authorName") String authorName,
//            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
//        
//        Optional<Document> existingDoc = documentRepository.findById(id);
//        if (!existingDoc.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Document not found"));
//        }
//
//        Document document = existingDoc.get();
//        document.setAuthor(authorName);
//        document.setUploadedAt(LocalDateTime.now()); // Update timestamp
//
//        if (file != null && !file.isEmpty()) {
//            String fileName = file.getOriginalFilename();
//            Path path = Paths.get("D:/uploads/" + fileName);
//
//            //  **Delete old file before saving the new one**
//            Path oldFilePath = Paths.get("D:/uploads/" + document.getFileName());
//            Files.deleteIfExists(oldFilePath);
//
//            //  **Save new file**
//            Files.write(path, file.getBytes());
//
//            // **Update document entity**
//            document.setFileName(fileName);
//            document.setFileType(file.getContentType());
//        }
//
//        documentRepository.save(document);
//
//        //  **Return response in JSON format**
//        return ResponseEntity.ok(Map.of("message", "Document updated successfully"));
//    }



    //  Delete document
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Document deleted successfully!");
        return ResponseEntity.ok(response);
    }
}



