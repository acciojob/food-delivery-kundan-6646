package com.driver.service.impl;


import com.driver.io.entity.FoodEntity;
import com.driver.io.repository.FoodRepository;
import com.driver.model.request.FoodDetailsRequestModel;
import com.driver.model.response.FoodDetailsResponse;
import com.driver.model.response.OperationStatusModel;
import com.driver.model.response.RequestOperationName;
import com.driver.model.response.RequestOperationStatus;
import com.driver.service.FoodService;
import com.driver.shared.dto.FoodDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FoodServiceImpl implements FoodService {
    @Autowired
    FoodRepository foodRepository;


    @Override
    public FoodDto createFood(FoodDto food) {
        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setFoodCategory(food.getFoodCategory());
        foodEntity.setFoodName(food.getFoodName());
        foodEntity.setFoodPrice(food.getFoodPrice());
        foodEntity.setFoodId(UUID.randomUUID().toString());

        foodRepository.save(foodEntity);
        food.setId(foodRepository.findByFoodId(food.getFoodId()).getId());
        return food;
    }

    @Override
    public FoodDto getFoodById(String foodId) throws Exception {
        FoodEntity foodEntity = foodRepository.findByFoodId(foodId);
        FoodDto foodDto = new FoodDto();
        foodDto.setFoodId(foodEntity.getFoodId());
        foodDto.setFoodName(foodEntity.getFoodName());
        foodDto.setFoodPrice(foodEntity.getFoodPrice());
        foodDto.setId(foodEntity.getId());
        foodDto.setFoodCategory(foodEntity.getFoodCategory());
        return foodDto;
    }

    @Override
    public FoodDto updateFoodDetails(String foodId, FoodDto foodDetails) throws Exception {
        FoodEntity foodEntity = foodRepository.findByFoodId(foodId);
        foodEntity.setFoodCategory(foodDetails.getFoodCategory());
        foodEntity.setFoodName(foodDetails.getFoodName());
        foodEntity.setFoodPrice(foodDetails.getFoodPrice());
        foodEntity.setFoodId(foodDetails.getFoodId());

        foodRepository.save(foodEntity);

        foodDetails.setFoodId(foodId);
        foodDetails.setId(foodEntity.getId());

        return foodDetails;
    }

    @Override
    public void deleteFoodItem(String id) throws Exception {
        long Id = foodRepository.findByFoodId(id).getId();
        foodRepository.deleteById(Id);
    }

    @Override
    public List<FoodDto> getFoods() {
        List<FoodEntity> list = (List<FoodEntity>) foodRepository.findAll();
        List<FoodDto> foodDtoList = new ArrayList<>();
        for(FoodEntity f : list){
            FoodDto foodDto = new FoodDto();
            foodDto.setId(f.getId());
            foodDto.setFoodId(f.getFoodId());
            foodDto.setFoodPrice(f.getFoodPrice());
            foodDto.setFoodName(f.getFoodName());
            foodDto.setFoodCategory(f.getFoodCategory());

            foodDtoList.add(foodDto);
        }

        return foodDtoList;
    }


    public FoodDetailsResponse get_Food(String id) throws Exception{
        FoodDto foodDto = getFoodById(id);
        return convertFoodDtoToFoodResponse(foodDto);
    }


    public FoodDetailsResponse createFood(FoodDetailsRequestModel foodDetails) {
        FoodDto foodDto = new FoodDto();
        foodDto.setFoodName(foodDetails.getFoodName());
        foodDto.setFoodCategory(foodDetails.getFoodCategory());
        foodDto.setFoodPrice(foodDetails.getFoodPrice());

        FoodDto finalFoodDto = createFood(foodDto);

        return convertFoodDtoToFoodResponse(finalFoodDto);
    }


    public FoodDetailsResponse updateFood(String id, FoodDetailsRequestModel foodDetails) throws Exception{
        FoodDto foodDto = new FoodDto();
        foodDto.setFoodName(foodDetails.getFoodName());
        foodDto.setFoodCategory(foodDetails.getFoodCategory());
        foodDto.setFoodPrice(foodDetails.getFoodPrice());

        FoodDto finalFoodDto = updateFoodDetails(id,foodDto);

        return convertFoodDtoToFoodResponse(finalFoodDto);
    }

    public OperationStatusModel deleteFood(String id) throws Exception{
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setOperationName(RequestOperationName.DELETE.toString());
        try{
            deleteFoodItem(id);
        } catch (Exception e){
            operationStatusModel.setOperationResult(RequestOperationStatus.ERROR.toString());
            return operationStatusModel;
        }

        operationStatusModel.setOperationResult(RequestOperationStatus.SUCCESS.toString());
        return operationStatusModel;
    }

    @GetMapping()
    public List<FoodDetailsResponse> get_Foods() {
        List<FoodDto> foodDtoList = getFoods();
        List<FoodDetailsResponse> foodDetailsResponseList = new ArrayList<>();
        for (FoodDto foodDto: foodDtoList) {
            foodDetailsResponseList.add(convertFoodDtoToFoodResponse(foodDto));
        }

        return foodDetailsResponseList;
    }

    public FoodDetailsResponse convertFoodDtoToFoodResponse(FoodDto foodDto) {
        FoodDetailsResponse foodDetailsResponse = new FoodDetailsResponse();
        foodDetailsResponse.setFoodId(foodDto.getFoodId());
        foodDetailsResponse.setFoodPrice(foodDto.getFoodPrice());
        foodDetailsResponse.setFoodCategory(foodDto.getFoodCategory());
        foodDetailsResponse.setFoodName(foodDto.getFoodName());

        return foodDetailsResponse;
    }
}