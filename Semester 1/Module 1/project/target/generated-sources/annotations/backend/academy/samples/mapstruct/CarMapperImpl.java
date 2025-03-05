package backend.academy.samples.mapstruct;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-21T13:10:00+0300",
    comments = "version: 1.6.0, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
public class CarMapperImpl implements CarMapper {

    @Override
    public CarDto carToCarDto(Car car) {
        if ( car == null ) {
            return null;
        }

        String manufacturer = null;
        int seatCount = 0;
        double mileageInKilometers = 0.0d;

        manufacturer = car.make();
        seatCount = car.numberOfSeats();
        mileageInKilometers = milesToKilometers( car.mileageInMiles() );

        CarDto carDto = new CarDto( manufacturer, seatCount, mileageInKilometers );

        return carDto;
    }

    @Override
    public PersonDto personToPersonDto(Person person) {
        if ( person == null ) {
            return null;
        }

        String fullName = null;

        fullName = person.name();

        PersonDto personDto = new PersonDto( fullName );

        return personDto;
    }
}
