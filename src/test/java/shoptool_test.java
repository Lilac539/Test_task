import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.commands.IsDisplayed;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.annotations.BeforeMethod;

import static org.hamcrest.Matchers.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.MatcherAssert.assertThat;


public class shoptool_test {

    @BeforeClass
    public static void setup(){open("https://shoptool.com.ua/");}

    SelenideElement mainCatalog = $x("//div[@class = 'span6 menu-grid ']");
    SelenideElement powerToolMenu = $x("//div[@class = 'span6 menu-grid ']//li[contains(.,'Электроинструмент')]");
    ElementsCollection allItems = $$x("//div[@class = 'ut2-cat-container reverse']//div[@class = 'ut2-gl__body']");
    SelenideElement pagesBlock = $x("//div[@class = 'ty-pagination__items']");
    SelenideElement menuPunchers = $x("//div[@class = 'ty-menu__submenu-item-header ']/a[contains(., 'Перфораторы')]");
    SelenideElement menuBasket = $x("//div[@class = 'ty-menu__submenu-item-header ']/a[contains(., 'Дрели')]");
    ElementsCollection prodWithSale = $$x("//div[@class = 'ut2-cat-container reverse']//div[text() = 'Скидка ']/ancestor::div[@class = 'ut2-gl__body']");

    @Test
    public void PuncherSalesPrice(){
        open("https://shoptool.com.ua/");

        SelenideElement oldPunchersPrice = $x("//div[@class = 'col-left']/div[@class = 'prices-container price-wrap']//span[@class = 'ty-strike']");
        ArrayList<Punchers> punchersInfo = new ArrayList<>();
        int punchersPage;
        String punchersName;

        mainCatalog.shouldBe(Condition.visible).click();
        powerToolMenu.shouldBe(Condition.visible).hover();
        menuPunchers.should(Condition.exist).click();
        for (int i=1; i<4; i++){
            if(thisPageExist(i)){
                goNextPage(i);
                if(!prodWithSale.isEmpty()){
                    for(SelenideElement el : prodWithSale){
                        punchersName = el.$x(".//div[@class = 'ut2-gl__name']").getText();
                        punchersPage = i;
                        punchersInfo.add(new Punchers(punchersPage, punchersName));
                    }
                }
            }
        }
        Collections.shuffle(punchersInfo);

        for (int i=0; i<3; i++){
            goNextPage(punchersInfo.get(i).getPunchPage());
            $x("//div[@class = 'ut2-cat-container reverse']//div[text() = 'Скидка ']" +
                    "/ancestor::div[@class = 'ut2-gl__body']//div[contains(.,'"+punchersInfo.get(i).getPunchName()+"')]").click();
            assertThat("В даної позиції немає акційної ціни: " + punchersInfo.get(i).getPunchName(), oldPunchersPrice.exists());
            back();
        }
    }

    @Test
    public void BasketChecking(){
        open("https://shoptool.com.ua/");

        int numberOfPage = 1;
        ArrayList<Integer> randomDrill = new ArrayList<Integer>();
        TreeSet<String> nameOfDrill = new TreeSet<String>();
        TreeSet<String> basketNameOfDrill = new TreeSet<String>();
        SelenideElement delFromBasket = $x("//td[@class = 'ty-cart-content__product-elem ty-cart-content__description']/a[@class = ' ty-cart-content__product-delete ty-delete-big']");
        ElementsCollection basketItemsName = $$x("//td[@class = 'ty-cart-content__product-elem ty-cart-content__description']/a[@class = 'ty-cart-content__product-title']");
        SelenideElement sumOfBasket = $x("//span[@class = 'ty-cart-statistic__value']");
        String firstSum;
        String secondSum;

        mainCatalog.shouldBe(Condition.visible).click();
        powerToolMenu.shouldBe(Condition.visible).hover();
        menuBasket.shouldBe(Condition.visible).click();
        while(numberOfPage <4) {
            if(thisPageExist(numberOfPage)){
                goNextPage(numberOfPage);
                randomDrill.addAll(getTheRandomId(allItems));
                nameOfDrill.add(allItems.get(randomDrill.get(0)).$x(".//div[@class = 'ut2-gl__name']").shouldBe(Condition.visible).getText());
                sleep(4000);
                allItems.get(randomDrill.get(0)).shouldBe(Condition.visible).click();
                $x("//button[contains(.,'В корзину')]").shouldBe(Condition.visible).click();
                sleep(2000);
                $x("//a[contains(.,'Продолжить покупки')]").shouldBe(Condition.enabled).click();
                back();
                numberOfPage++;
            }
        }
        $x("//div[@class = 'ty-dropdown-box']//span[contains(., 'Корзина')]").shouldBe(Condition.visible).click();
        $x("//div[@class = 'ty-float-left']").shouldBe(Condition.enabled).click();
        for(SelenideElement drillName : basketItemsName){
            basketNameOfDrill.add(drillName.getText());
        }
        assertThat("В корзині знаходяться не ті товари, що купували", nameOfDrill.equals(basketNameOfDrill));
        firstSum = sumOfBasket.getText();
        delFromBasket.shouldBe(Condition.visible).click();
        secondSum = sumOfBasket.getText();
        assertThat("Після видалення сума не змінилась", !firstSum.equals(secondSum));
    }

    @Test
    public void procraftChecking(){

        ElementsCollection procraftTitles = $$x("//div[@class = 'ut2-cat-container reverse']//img[@title = 'PROCRAFT']");
        int numberOfPage = 1;
        String testWord = "Makita";
        TreeSet<String> itemsFromProcraft = new TreeSet<String>();
        ArrayList<Integer> itemsPage = new ArrayList<Integer>();
        ArrayList<String> itemsWithoutTestWord = new ArrayList<String>();

        mainCatalog.shouldBe(Condition.visible).click();
        powerToolMenu.shouldBe(Condition.visible).hover();
        menuPunchers.should(Condition.enabled).click();

        while(numberOfPage < 3){
            if(thisPageExist(numberOfPage)){
                goNextPage(numberOfPage);
                for( SelenideElement procraftEl: procraftTitles ){
                    if (!procraftTitles.isEmpty()){
                        System.out.println(procraftEl.$x("./ancestor::div[@class = 'ut2-gl__body']/div[@class = 'ut2-gl__name']").shouldBe(Condition.visible).getText());}
                }
                numberOfPage++;
            }
        }
        for (int i=1; i<6; i+=2){
            if(thisPageExist(i)){
                goNextPage(i);
                for( SelenideElement procraftEl: procraftTitles ){
                    if (!procraftTitles.isEmpty()){
                        itemsFromProcraft.add(procraftEl.$x("./ancestor::div[@class = 'ut2-gl__body']/div[@class = 'ut2-gl__name']").shouldBe(Condition.visible).getText());
                        itemsPage.add(i);
                    }
                }
            }
        }
        for(String el: itemsFromProcraft){
            if (!el.contains(testWord)){
                itemsWithoutTestWord.add(el);
            }
        }

        Collections.shuffle(itemsPage);
        if (thisPageExist(itemsPage.get(0))){
            goNextPage(itemsPage.get(0));
            procraftTitles.get(0).shouldBe(Condition.visible).click();
        }

        assertThat("В даних найменуваннях немає слова Makita: " + itemsWithoutTestWord.toString(), itemsWithoutTestWord.isEmpty());


    }

    @Test
    public void SalePriceCalculate(){

        mainCatalog.shouldBe(Condition.visible).click();
        powerToolMenu.shouldBe(Condition.visible).hover();
        sleep(2000);
        menuBasket.shouldBe(Condition.visible).click();
        SelenideElement showMoreItems = $x("//span[@class = 'ut2-load-more']");
        String pathNewPrice = ".//span[@class = 'ty-price-num'][not(contains(., 'грн'))]";
        String pathToName = "./div[@class = 'ut2-gl__name']";
        String pathOldPrice = ".//span[@class = 'ty-strike']";
        String pathDiscPercent = ".//div[@class = 'ty-product-labels__content']";
        double newPrice, oldPrice, discPercent, expectedPrice;

        while(prodWithSale.size() < 5){
            showMoreItems.shouldBe(Condition.visible).click();
        }

        for(SelenideElement el:prodWithSale){
                discPercent = Integer.parseInt(el.$x(pathDiscPercent).shouldBe(Condition.visible).getText().replaceAll("\\W", ""));
                newPrice = Double.parseDouble(el.$x(pathNewPrice).getText().replaceAll(",", ""));
                oldPrice = Double.parseDouble(el.$x(pathOldPrice).getText().replaceAll(",", "").replaceAll(" грн", ""));
                expectedPrice = oldPrice - (oldPrice * discPercent/100);

                assertThat(el.$x(pathToName).getText(), newPrice, equalTo(expectedPrice));
        }
    }

    public void goNextPage (int page){
            if (pagesBlock.$x("./a[text() = '"+ page + "']").exists()){
                pagesBlock.$x("./a[text() = '"+ page + "']").shouldBe(Condition.visible).click();
                sleep(2000);
            }
    }

    public boolean thisPageExist (int page){
        if(pagesBlock.$x("./span[text() = '"+ page +"']").exists()||pagesBlock.$x("./a[text() = '"+ page + "']").exists()){
            return true;
        }
        return false;
    }

    public ArrayList<Integer> getTheRandomId(ElementsCollection someCollection){
        ArrayList<Integer> listRndm = new ArrayList();
        for (int i = 0; i<someCollection.size(); i++){
            listRndm.add(i);
        }
        Collections.shuffle(listRndm);
        return listRndm;
    }
}
