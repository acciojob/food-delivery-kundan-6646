package com.driver.service.impl;


import com.driver.model.request.FoodDetailsRequestModel;
import com.driver.model.response.FoodDetailsResponse;
import com.driver.model.response.OperationStatusModel;
import com.driver.service.FoodService;
import com.driver.shared.dto.FoodDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class FoodServiceImpl {
    @Autowired
    FoodService foodService;
    public FoodDetailsResponse getFoodById(String id) throws Exception{
        FoodDto food = foodService.getFoodById(id);
        return getFoodResponseDTO(food);
    }

    public FoodDetailsResponse createFood(FoodDetailsRequestModel foodDetails) {
        FoodDto foodDto = new FoodDto();
        foodDto.setFoodCategory(foodDetails.getFoodCategory());
        foodDto.setFoodName(foodDetails.getFoodName());
        foodDto.setFoodPrice(foodDetails.getFoodPrice());

        foodDto = foodService.createFood(foodDto);

        return getFoodResponseDTO(foodDto);
    }

    public FoodDetailsResponse updateFood(String id, FoodDetailsRequestModel foodDetails) throws Exception{
        FoodDto foodDto = foodService.getFoodById(id);
        foodDto.setFoodCategory(foodDetails.getFoodCategory());
        foodDto.setFoodName(foodDetails.getFoodName());
        foodDto.setFoodPrice(foodDetails.getFoodPrice());

        foodDto = foodService.updateFoodDetails(id, foodDto);

        return getFoodResponseDTO(foodDto);
    }

    public OperationStatusModel deleteFood(String id) throws Exception{
        foodService.deleteFoodItem(id);
        return new OperationStatusModel();
    }

    public List<FoodDetailsResponse> getFoods() {
        List<FoodDto> foodDtoList = foodService.getFoods();
        List<FoodDetailsResponse> foodDetailsResponseList = new ArrayList<>();
        for(FoodDto foodDto: foodDtoList) {
            foodDetailsResponseList.add(getFoodResponseDTO(foodDto));
        }

        return foodDetailsResponseList;
    }

    private FoodDetailsResponse getFoodResponseDTO(FoodDto food) {
        FoodDetailsResponse foodDetailsResponse = new FoodDetailsResponse();
        foodDetailsResponse.setFoodCategory(food.getFoodCategory());
        foodDetailsResponse.setFoodName(food.getFoodName());
        foodDetailsResponse.setFoodPrice(food.getFoodPrice());
        foodDetailsResponse.setFoodId(food.getFoodId());

        return foodDetailsResponse;
    }
}