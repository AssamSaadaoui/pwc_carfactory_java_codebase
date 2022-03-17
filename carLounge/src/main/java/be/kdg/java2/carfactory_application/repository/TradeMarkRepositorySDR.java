package be.kdg.java2.carfactory_application.repository;

import be.kdg.java2.carfactory_application.domain.factory.TradeMark;
import org.springframework.data.jpa.repository.JpaRepository;

//@Profile("SDR")
public interface TradeMarkRepositorySDR extends JpaRepository<TradeMark, Integer> {
}
