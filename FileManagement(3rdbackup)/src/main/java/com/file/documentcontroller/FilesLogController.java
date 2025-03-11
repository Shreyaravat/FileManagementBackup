package com.file.documentcontroller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.entity.FilesLog;

import com.file.entity.Users;import com.file.entity.Users;
import com.file.repo.FilesLogRepository;
 
@RestController
@RequestMapping("/api/files")
@CrossOrigin("*")
public class FilesLogController 
{
 
    @Autowired
    private FilesLogRepository filesLogRepository;
 
//    @PostMapping("/save")
//    public ResponseEntity<String> saveFileLog(@RequestBody FilesLog filesLog)
//    {
//        filesLogRepository.save(filesLog);
//        return ResponseEntity.ok("File Log Saved Successfully");
//    } 
    @PostMapping("/save")
    public ResponseEntity<String> saveFileLog(@RequestBody FilesLog filesLog)
    {
        if (filesLog.getLastModified() == null)
        {
            filesLog.setLastModified("Doc Uploaded");
        }
        
        if (filesLog.getLastModifiedBy() == null) 
        {
        	 Users defaultUser = new Users();
             defaultUser.setUsername("Doc Uploaded");
             filesLog.setLastModifiedBy(defaultUser);        
        }
        
        filesLogRepository.save(filesLog);
        return ResponseEntity.ok("File Log Saved Successfully");
    }

    @GetMapping("/all")
    public List<FilesLog> getAllFiles() {
        return filesLogRepository.findAll();
    }
}


