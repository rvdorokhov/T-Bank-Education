package backend.academy;

import backend.academy.transformations.Transformation;
import backend.academy.transformations.nonlinear.DiskTransformation;
import backend.academy.transformations.nonlinear.HeartTransformation;
import backend.academy.transformations.nonlinear.PolarTransformation;
import backend.academy.transformations.nonlinear.SinTransformation;
import backend.academy.transformations.nonlinear.SphereTransformation;
import backend.academy.user.session.UserInterface;
import backend.academy.user.session.UserSession;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        List<Transformation> nonLinearVariations = new ArrayList<>();
        Transformation nonLinearTrans1 = new SphereTransformation();
        nonLinearVariations.add(nonLinearTrans1);
        Transformation nonLinearTrans2 = new SinTransformation();
        nonLinearVariations.add(nonLinearTrans2);
        Transformation nonLinearTrans3 = new PolarTransformation();
        nonLinearVariations.add(nonLinearTrans3);
        Transformation nonLinearTrans4 = new HeartTransformation();
        nonLinearVariations.add(nonLinearTrans4);
        Transformation nonLinearTrans5 = new DiskTransformation();
        nonLinearVariations.add(nonLinearTrans5);

        UserInterface userInterface = new UserSession(System.out, System.in, nonLinearVariations);
        userInterface.start();
    }
}
