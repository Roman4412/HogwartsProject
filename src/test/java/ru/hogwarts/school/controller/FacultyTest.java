package ru.hogwarts.school.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepo;
import ru.hogwarts.school.repository.StudentRepo;
import ru.hogwarts.school.service.FacultyService;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepo facultyRepository;

    @MockBean
    private StudentRepo studentRepository;

    @SpyBean
    private FacultyService facultyService;

    @SpyBean
    private FacultyMapper facultyMapper;

    @SpyBean
    private StudentMapper studentMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void createTest() throws Exception {
        FacultyDtoIn facultyDtoIn = createDtoIn();
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName(facultyDtoIn.getName());
        faculty.setColor(facultyDtoIn.getColor());
        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/faculty")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(facultyDtoIn))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(facultyDtoIn.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
                });
        verify(facultyRepository, new Times(1)).save(any());
    }

    @Test
    public void updateTest() throws Exception {
        FacultyDtoIn facultyDtoIn = createDtoIn();

        Faculty oldFaculty = createFaculty(1);

        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(oldFaculty));

        oldFaculty.setColor(facultyDtoIn.getColor());
        oldFaculty.setName(facultyDtoIn.getName());
        when(facultyRepository.save(any())).thenReturn(oldFaculty);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/faculty/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(facultyDtoIn))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(facultyDtoIn.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
                });
        verify(facultyRepository, Mockito.times(1)).save(any());
        Mockito.reset(facultyRepository);
    }

    @Test
    public void getTest() throws Exception {
        Faculty faculty = createFaculty(1);

        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/1")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(faculty.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(faculty.getName());
                });
    }

    @Test
    public void deleteTest() throws Exception {
        Faculty faculty = createFaculty(1);

        when(facultyRepository.findById(eq(1L))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculty/1")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(faculty.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(faculty.getName());
                });
        verify(facultyRepository, times(1)).delete(any());
        Mockito.reset(facultyRepository);
    }

    @Test
    public void findAllTest() throws Exception {
        List<Faculty> faculties = Stream.iterate(1, id -> id + 1)
                .map(this::createFaculty)
                .limit(20)
                .toList();



        String name = faculties.get(0).getName();
        faculties = faculties.stream()
                .filter(faculty -> faculty.getName().equals(name))
                .collect(Collectors.toList());
        List<FacultyDtoOut> expectedResult2 = faculties.stream()
                .filter(faculty -> faculty.getName().equals(name))
                .map(facultyMapper::toDto)
                .toList();
        when(facultyRepository.findAllByNameContainingIgnoreCase(eq(name))).thenReturn(faculties);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculty/filter?name=" + name)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<FacultyDtoOut> facultyDtoOuts = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(facultyDtoOuts)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(facultyDtoOuts.size())
                            .forEach(index -> {
                                FacultyDtoOut facultyDtoOut = facultyDtoOuts.get(index);
                                FacultyDtoOut expected = expectedResult2.get(index);
                                assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                assertThat(facultyDtoOut.getColor()).isEqualTo(expected.getColor());
                                assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                            });
                });
    }


    private FacultyDtoIn createDtoIn() {
        FacultyDtoIn facultyDtoIn = new FacultyDtoIn();
        facultyDtoIn.setName(faker.harryPotter().house());
        facultyDtoIn.setColor(faker.color().name());
        return facultyDtoIn;
    }

    private Faculty createFaculty(long id) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return faculty;
    }

}
