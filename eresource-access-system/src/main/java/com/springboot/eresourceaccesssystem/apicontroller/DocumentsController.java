package com.springboot.eresourceaccesssystem.apicontroller;

import com.mongodb.lang.NonNull;
import com.springboot.eresourceaccesssystem.model.Comments;
import com.springboot.eresourceaccesssystem.model.DocumentElastic;
import com.springboot.eresourceaccesssystem.model.DocumentMongo;
import com.springboot.eresourceaccesssystem.service.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping("api/documents")
public class DocumentsController {
    private final DocumentsService documentsService;

    @Autowired
    public DocumentsController(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    @PostMapping("/createIndex/{indexName}")
    public boolean createIndex(@PathVariable("indexName") String indexName) throws IOException { return documentsService.createIndex(indexName); }

    @GetMapping("/checkIndex/{indexName}")
    public boolean checkIndex(@PathVariable("indexName") String indexName) throws IOException { return documentsService.checkIndex(indexName); }

    @DeleteMapping("/deleteIndex/{indexName}")
    public boolean deleteIndex(@PathVariable("indexName") String indexName) throws IOException { return documentsService.deleteIndex(indexName); }

    @PostMapping("/insert")
    public void insertDocuments(@Valid @NonNull @RequestBody DocumentMongo documents) {
        this.documentsService.insertDocuments(documents);
    }

    @GetMapping("/getAll")
    public List<DocumentMongo> getAllDocuments() {
        return this.documentsService.getAllDocuments();
    }

    @GetMapping("/getAllElastic")
    public List<DocumentElastic> getAllDocumentsElastic() {
        return documentsService.getAllDocumentsElastic();
    }

    @GetMapping("/get/{id}")
    public DocumentMongo findDocumentsById(@PathVariable("id") String id) {
        return documentsService.findDocumentsById(id)
                .orElse(null);
    }

    @GetMapping("/find/{searchText}")
    public List<Optional<DocumentMongo>> findDocuments(@PathVariable("searchText") String searchText) throws IOException {
        return documentsService.findDocuments(
                Optional.ofNullable(searchText),
                Optional.ofNullable(searchText),
                Optional.ofNullable(searchText));
    }

    @DeleteMapping("/deleteAll")
    public void deleteAllDocuments() {
        this.documentsService.deleteAllDocuments();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDocumentsById(@PathVariable("id") String id) {
        documentsService.deleteDocumentsById(id);
    }

    @PutMapping("/update/{id}")
    public void updateDocumentsById(@PathVariable("id") String id, @RequestBody DocumentMongo documents) {
        documentsService.updateDocumentsById(id, documents);
    }

    @PutMapping("/addComment/{id}")
    public void addComment(@PathVariable("id") String id, @RequestBody Comments comments) {
        documentsService.addComment(id, comments);
    }
}
