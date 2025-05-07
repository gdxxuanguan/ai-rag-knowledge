package com.xuanguan.dev.api;

import com.xuanguan.dev.api.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IAIRAGService {

    Response<List<String>> queryRAGTagList();

    Response<String> uploadFile(String ragTag, List<MultipartFile> files);

}
