package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.common.UtilityProvider;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @CrossOrigin
    @RequestMapping(path = "/restaurant", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getAllRestaurants(){
        final List<RestaurantEntity> restaurantEntityList = restaurantService.getAllRestaurants();
        List<RestaurantDetailsResponse> restaurantDetailsResponsesList = new ArrayList<>();
        for(int i=0;i<restaurantEntityList.size();i++){
            RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
            RestaurantEntity restaurantEntityTmp = restaurantEntityList.get(i);

            //Restaurant Details
            restaurantDetailsResponse.id(UUID.fromString(restaurantEntityTmp.getUuid()));
            restaurantDetailsResponse.restaurantName(restaurantEntityTmp.getRestaurantName());
            restaurantDetailsResponse.photoURL(restaurantEntityTmp.getPhotoUrl());
            restaurantDetailsResponse.customerRating(restaurantEntityTmp.getCustomerRating());
            restaurantDetailsResponse.averagePrice(restaurantEntityTmp.getAveragePrice());
            restaurantDetailsResponse.numberCustomersRated(restaurantEntityTmp.getNoOfCustomerRated());

            //Restaurant Address details
               RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
               AddressEntity addressEntity =  restaurantService.getRestaurantAddress(restaurantEntityTmp.getId());
               restaurantDetailsResponseAddress.city(addressEntity.getCity());
               restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlatBuilNo());
               restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
               restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
               restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());

                     //Restaurant Address State Details
                     RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
                     StateEntity stateEntity = addressEntity.getState();
                     restaurantDetailsResponseAddressState.id(UUID.fromString(stateEntity.getUuid()));
                     restaurantDetailsResponseAddressState.stateName(stateEntity.getStateName());

                 restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);
               restaurantDetailsResponse.address(restaurantDetailsResponseAddress);

             //Restaurant Category Details
             List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantService.getCategories(restaurantEntityTmp);

                  List<CategoryList> categoryLists = new ArrayList<>();
                   for (int j = 0; j < restaurantCategoryEntities.size(); j++) {
                       CategoryEntity categoryEntity = restaurantCategoryEntities.get(j).getCategoryEntity();
                       CategoryList categoryList = new CategoryList();
                       categoryList.categoryName(categoryEntity.getCategoryName());
                       categoryLists.add(categoryList);
                   }

              restaurantDetailsResponse.categories(categoryLists);

            restaurantDetailsResponsesList.add(restaurantDetailsResponse);
            }

        return new ResponseEntity<>(restaurantDetailsResponsesList,HttpStatus.OK);
    }



//
//    get restaurant by restaurant Names
//
//
//
//

    @CrossOrigin
    @RequestMapping(path = "/restaurant/name/{restaurant_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>>
                   getAllRestaurantsByName(@PathVariable("restaurant_name") final String RestaurantName){
        final List<RestaurantEntity> restaurantEntityList = restaurantService.getAllRestaurantsByName(RestaurantName);
          List<RestaurantDetailsResponse> restaurantDetailsResponsesList = new ArrayList<>();
          for(int i=0;i<restaurantEntityList.size();i++){
              RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
               RestaurantEntity restaurantEntityTmp = restaurantEntityList.get(i);

               //Restaurant Details
               restaurantDetailsResponse.id(UUID.fromString(restaurantEntityTmp.getUuid()));
               restaurantDetailsResponse.restaurantName(restaurantEntityTmp.getRestaurantName());
               restaurantDetailsResponse.photoURL(restaurantEntityTmp.getPhotoUrl());
               restaurantDetailsResponse.customerRating(restaurantEntityTmp.getCustomerRating());
               restaurantDetailsResponse.averagePrice(restaurantEntityTmp.getAveragePrice());
               restaurantDetailsResponse.numberCustomersRated(restaurantEntityTmp.getNoOfCustomerRated());

              //Restaurant Address details
               RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
               AddressEntity addressEntity =  restaurantService.getRestaurantAddress(restaurantEntityTmp.getId());
               restaurantDetailsResponseAddress.city(addressEntity.getCity());
               restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlatBuilNo());
               restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
               restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
               restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());

                     //Restaurant Address State details
                     RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
                     StateEntity stateEntity = addressEntity.getState();
                     restaurantDetailsResponseAddressState.id(UUID.fromString(stateEntity.getUuid()));
                     restaurantDetailsResponseAddressState.stateName(stateEntity.getStateName());
                     restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

                restaurantDetailsResponse.address(restaurantDetailsResponseAddress);

              //Restaurant Category Details
              List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantService.getCategories(restaurantEntityTmp);

              List<CategoryList> categoryLists = new ArrayList<>();
              for (int j = 0; j < restaurantCategoryEntities.size(); j++) {
                  CategoryEntity categoryEntity = restaurantCategoryEntities.get(j).getCategoryEntity();
                  CategoryList categoryList = new CategoryList();
                  categoryList.categoryName(categoryEntity.getCategoryName());
                  categoryLists.add(categoryList);
              }

              restaurantDetailsResponse.categories(categoryLists);

              restaurantDetailsResponsesList.add(restaurantDetailsResponse);
         }
       return new ResponseEntity<>(restaurantDetailsResponsesList,HttpStatus.OK);
    }

//
//
//    Get all restaurants by restaurantId -> UUid
//
//
        @CrossOrigin
        @RequestMapping(path = "/api/restaurant/{restaurant_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<RestaurantDetailsResponse>
                        getAllRestaurantsByUuid(
                                @PathVariable("restaurant_id") final String restaurantId)
                                    throws RestaurantNotFoundException {
            final RestaurantEntity restaurantEntity = restaurantService.getRestaurantByUuid(restaurantId);
            RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();

            //Restaurant Details
            restaurantDetailsResponse.id(UUID.fromString(restaurantEntity.getUuid()));
            restaurantDetailsResponse.restaurantName(restaurantEntity.getRestaurantName());
            restaurantDetailsResponse.photoURL(restaurantEntity.getPhotoUrl());
            restaurantDetailsResponse.customerRating(restaurantEntity.getCustomerRating());
            restaurantDetailsResponse.averagePrice(restaurantEntity.getAveragePrice());
            restaurantDetailsResponse.numberCustomersRated(restaurantEntity.getNoOfCustomerRated());

                //Restaurant Address details
                RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
                AddressEntity addressEntity =  restaurantService.getRestaurantAddress(restaurantEntity.getId());
                restaurantDetailsResponseAddress.city(addressEntity.getCity());
                restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlatBuilNo());
                restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
                restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
                restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());

                    //Restaurant Address State details
                    RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
                    StateEntity stateEntity = addressEntity.getState();
                    restaurantDetailsResponseAddressState.id(UUID.fromString(stateEntity.getUuid()));
                    restaurantDetailsResponseAddressState.stateName(stateEntity.getStateName());
                    restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);
                restaurantDetailsResponse.address(restaurantDetailsResponseAddress);

            //Restaurant Category Details
            List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantService.getCategories(restaurantEntity);
            List<CategoryList> categoryLists = new ArrayList<>();
            for (int j = 0; j < restaurantCategoryEntities.size(); j++) {
                CategoryEntity categoryEntity = restaurantCategoryEntities.get(j).getCategoryEntity();
                CategoryList categoryList = new CategoryList();
                categoryList.categoryName(categoryEntity.getCategoryName());
                categoryLists.add(categoryList);
            }

            restaurantDetailsResponse.categories(categoryLists);

        return new ResponseEntity<>(restaurantDetailsResponse,HttpStatus.OK);
}

//
//
//Get restaurants by CategoryID
//

    @CrossOrigin
    @RequestMapping(path = "/restaurant/category/{category_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>>
    getRestaurantByCategoryId(@PathVariable("category_id") final String category_id) throws CategoryNotFoundException {
        final List<RestaurantEntity> restaurantEntityList = restaurantService.getRestaurantByCategory(category_id);
        List<RestaurantDetailsResponse> restaurantDetailsResponsesList = new ArrayList<>();
        for(int i=0;i<restaurantEntityList.size();i++){
            RestaurantDetailsResponse restaurantDetailsResponse = new RestaurantDetailsResponse();
            RestaurantEntity restaurantEntityTmp = restaurantEntityList.get(i);

            //Restaurant Details
            restaurantDetailsResponse.id(UUID.fromString(restaurantEntityTmp.getUuid()));
            restaurantDetailsResponse.restaurantName(restaurantEntityTmp.getRestaurantName());
            restaurantDetailsResponse.photoURL(restaurantEntityTmp.getPhotoUrl());
            restaurantDetailsResponse.customerRating(restaurantEntityTmp.getCustomerRating());
            restaurantDetailsResponse.averagePrice(restaurantEntityTmp.getAveragePrice());
            restaurantDetailsResponse.numberCustomersRated(restaurantEntityTmp.getNoOfCustomerRated());

            //Restaurant Address details
            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            AddressEntity addressEntity =  restaurantService.getRestaurantAddress(restaurantEntityTmp.getId());
            restaurantDetailsResponseAddress.city(addressEntity.getCity());
            restaurantDetailsResponseAddress.flatBuildingName(addressEntity.getFlatBuilNo());
            restaurantDetailsResponseAddress.id(UUID.fromString(addressEntity.getUuid()));
            restaurantDetailsResponseAddress.locality(addressEntity.getLocality());
            restaurantDetailsResponseAddress.pincode(addressEntity.getPincode());

            //Restaurant Address State details
            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();
            StateEntity stateEntity = addressEntity.getState();
            restaurantDetailsResponseAddressState.id(UUID.fromString(stateEntity.getUuid()));
            restaurantDetailsResponseAddressState.stateName(stateEntity.getStateName());
            restaurantDetailsResponseAddress.state(restaurantDetailsResponseAddressState);

            restaurantDetailsResponse.address(restaurantDetailsResponseAddress);

            //Restaurant Category Details
            List<RestaurantCategoryEntity> restaurantCategoryEntities = restaurantService.getCategories(restaurantEntityTmp);

            List<CategoryList> categoryLists = new ArrayList<>();
            for (int j = 0; j < restaurantCategoryEntities.size(); j++) {
                CategoryEntity categoryEntity = restaurantCategoryEntities.get(j).getCategoryEntity();
                CategoryList categoryList = new CategoryList();
                categoryList.categoryName(categoryEntity.getCategoryName());
                categoryLists.add(categoryList);
            }

            restaurantDetailsResponse.categories(categoryLists);

            restaurantDetailsResponsesList.add(restaurantDetailsResponse);
        }
        return new ResponseEntity<>(restaurantDetailsResponsesList,HttpStatus.OK);
    }


///////
//
//
//    Update Restaurant Details -Not Completed
//

    @RequestMapping(method = RequestMethod.PUT,path="/api/restaurant/{restaurant_id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> editRestaurant(
            @RequestHeader("authorization") final String authorization,
            @RequestParam Double customerRating,
            @PathVariable("restaurant_id") final String restaurantUuid)
                throws RestaurantNotFoundException,
                       AuthorizationFailedException,
                       InvalidRatingException {
        String accessToken = UtilityProvider.getAccessTokenFromAuthorization(authorization);
        final RestaurantEntity restaurantEntity = restaurantService.editRestaurantEntity(restaurantUuid,customerRating,accessToken);

        RestaurantUpdatedResponse restaurantUpdatedResponse = new RestaurantUpdatedResponse()
                .id(UUID.fromString(restaurantEntity.getUuid())).status("RESTAURANT RATING UPDATED SUCCESSFULLY");



    return new ResponseEntity<>(restaurantUpdatedResponse,HttpStatus.OK);
    }

}
