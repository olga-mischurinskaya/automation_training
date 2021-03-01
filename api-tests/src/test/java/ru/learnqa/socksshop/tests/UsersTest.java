package ru.learnqa.socksshop.tests;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.learnqa.socksshop.Cards;
import ru.learnqa.socksshop.UserPayload;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;


public class UsersTest {


	@BeforeClass
	public void setUp() {
		RestAssured.baseURI = ("http://217.28.229.104/");
	}

	@Test
    public void testCanBeRegistrationNewUser() {
		UserPayload user = new UserPayload();
				user.setEmail("test2@mail.gov");
				user.setUsername(RandomStringUtils.randomAlphanumeric(8));
				user.setPassword("test1234");
				RestAssured.given().contentType(ContentType.JSON).log().all()
				.body(user)
				.when()
				.post("register")
				.then().log().all()
				.assertThat()
				.statusCode(200)
				.body("id", is(not(emptyString())));

	}

	@Test
	public void testAddCard() {
		UserPayload user = new UserPayload();
		user.setEmail("test2@mail.gov");
		user.setUsername(RandomStringUtils.randomAlphanumeric(8));
		user.setPassword("test1234");

		ValidatableResponse resp = given().contentType(ContentType.JSON).log().all()
				.body(user)
				.when()
				.post("register")
				.then().log().all()
				.assertThat()
				.statusCode(200);

		//Сохраняем cockies
		Cookies cockies = resp.extract().detailedCookies();

		Cards card = new Cards();
		//card.setLongNum(RandomStringUtils.randomNumeric(16));
		card.setLongNum("1234567890123456");
		card.setExpires("01/23");
		card.setCcv(RandomStringUtils.randomNumeric(3));

		JsonPath resp1 = RestAssured.given().contentType(ContentType.JSON).log().all()
				.body(card)
				.cookies(cockies)
				.when()
				.post("cards")
				.then().log().all()
				.assertThat()
				.statusCode(200)
				.extract()
				.jsonPath();

		JsonPath resp2 = given()
				.cookies(cockies)
				.when()
				.get("card")
				.then()
				.assertThat()
				.statusCode(200)
				.extract()
				.jsonPath();

		System.out.println("number = " + resp2.getString("number"));

		System.out.println("resp = " + resp.extract().jsonPath().prettify());
		System.out.println("resp1 = " + resp1.prettify());
		System.out.println("resp2 = " + resp2.prettify());
	}

	@Test
    public void testCanNotRegisterSameUserTwice(){
        UserPayload user = new UserPayload();
        user.setEmail("test2@mail.gov");
        user.setUsername(RandomStringUtils.randomAlphanumeric(8));
        user.setPassword("test1234");

        RestAssured.given().contentType(ContentType.JSON).log().all()
                .body(user)
                .when()
                .post("register")
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .body("id", is(not(emptyString())));

        RestAssured.given().contentType(ContentType.JSON).log().all()
                .body(user)
                .when()
                .post("register")
                .then().log().all()
                .assertThat()
                .statusCode(500);
   }


}
