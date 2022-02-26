package be.kdg.java2.carfactory_application.presentation.controller.mvc.viewmodel;

import be.kdg.java2.carfactory_application.domain.Car;
import be.kdg.java2.carfactory_application.domain.Engineer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ViewModelMapper {

//    public ViewModelMapper() {
//    }
//
//    public List<EngineerViewModel> engineerToViewModel(List<EngineerViewModel> engineers) {
//        return engineers
//                .stream()
//                .map(this::toViewModel)
//                .collect(Collectors.toList());
//    }
//
//    public EngineerViewModel toViewModel(Engineer engineer) {
//        var result = new EngineerViewModel();
//        copyFieldsToViewModel(result, engineer);
//        return result;
//    }
//
//
//    private void copyFieldsToViewModel(EngineerViewModel engineerViewModel, Engineer engineer) {
//        engineerViewModel.setId(engineer.getId());
//        engineerViewModel.setName(engineer.getName());
//        engineerViewModel.setNationality(engineer.getNationality());
//    }
}
