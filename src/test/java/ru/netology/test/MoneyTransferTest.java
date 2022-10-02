package ru.netology.test;

import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.*;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTransferTest {

    @BeforeEach
    public void setUpAll() {
        open("http://localhost:9999");
        var loginPage = new LoginPageV1();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferMoneyFromCard2ToCard1() {

        var cardsInfo = DataHelper.getCardsInfo();
        var cards = new DashboardYourCards();
        int firstBalanceBefore = cards.getFirstCardBalance();
        int secondBalanceBefore = cards.getSecondCardBalance();
        int replenishSum = 100;
        var replenishThisCard = cards.replenishFirst();
        replenishThisCard.replenish(Integer.toString(replenishSum), cardsInfo, 1);
        assertEquals(firstBalanceBefore + replenishSum, cards.getFirstCardBalance());
        assertEquals(secondBalanceBefore - replenishSum, cards.getSecondCardBalance());
    }

    @Test
    void shouldTransferMoneyFromCard1ToCard2() {

        var cardsInfo = DataHelper.getCardsInfo();
        var cards = new DashboardYourCards();
        int firstBalanceBefore = cards.getFirstCardBalance();
        int secondBalanceBefore = cards.getSecondCardBalance();
        int replenishSum = 100;
        var replenishCard = cards.replenishSecond();
        replenishCard.replenish(Integer.toString(replenishSum), cardsInfo, 2);
        assertEquals(firstBalanceBefore - replenishSum, cards.getFirstCardBalance());
        assertEquals(secondBalanceBefore + replenishSum, cards.getSecondCardBalance());
    }

    @Disabled
    @Test
    void shouldTransferMoneyBetweenOwnCardsV3() {
        // тест падает - проводит перевод большей суммы, чем есть в наличии - баг
        var cardsInfo = DataHelper.getCardsInfo();
        var cards = new DashboardYourCards();
        var replenish = new ReplenishCards();
        int firstBalanceBefore = cards.getFirstCardBalance();
        int secondBalanceBefore = cards.getSecondCardBalance();
        int replenishSum = 50000;

        var replenishCard = cards.replenishSecond();
        replenishCard.replenish(Integer.toString(replenishSum), cardsInfo, 2);
        replenish.errorIfTransferIsMoreThenBalance();
        assertEquals(firstBalanceBefore, cards.getFirstCardBalance());
        assertEquals(secondBalanceBefore, cards.getSecondCardBalance());
    }
}

