package com.comp.comp_web.domain.archive.controller;

import com.comp.comp_web.domain.archive.dto.response.ArchiveProjectResponse;
import com.comp.comp_web.domain.archive.service.ArchiveProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @RestController: "스프링아, 얘는 JSON 데이터를 손님에게 반환하는 웨이터야!" 라고 알려줍니다.
@RestController
// @RequestMapping: 이 웨이터가 담당하는 기본 구역(URL)을 설정합니다.
@RequestMapping("/archive")
@RequiredArgsConstructor
public class ArchiveProjectController {

    // 웨이터가 주문을 넘길 주방장(Service)을 연결합니다.
    private final ArchiveProjectService archiveProjectService;

    // @GetMapping: 손님이 "GET /archive/projects"로 요청을 보내면 이 메서드가 실행됩니다.
    @GetMapping("/projects")
    public ResponseEntity<List<ArchiveProjectResponse>> getProjects(
        // @RequestParam: 주소창에 적힌 "?generation=39" 값을 뽑아옵니다.
        // required = false로 설정해서, 값을 안 보내도 에러가 나지 않게 합니다.
        @RequestParam(value = "generation", required = false) Integer generation) {

        // 1. 주방장(Service)에게 기수 정보를 주면서 요리를 부탁합니다.
        List<ArchiveProjectResponse> response = archiveProjectService.getProjects(generation);

        // 2. 완성된 요리를 "200 OK" 상태 코드와 함께 손님에게 서빙합니다.
        return ResponseEntity.ok(response);
    }
}
