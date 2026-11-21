import java.util.Scanner;

public class PushtopassSimulator {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        double mass = 768.0;
        double rho = 1.225;
        double area = 1.45;
        double cd = 0.88;
        double power = 750000.0;
        double maxgrip = 11500;
        double dt = 0.05;

        System.out.println("F1 Push to pass Simulator");
        System.out.print("Enter straight length in meters (eg:- 1200 for Monza, 2200 for Baku): ");
        double sln = sc.nextDouble();

        System.out.print("Enter starting gap between cars in meters (eg:- 45): ");
        double lcp = sc.nextDouble();

        System.out.print("Enter corner exit speed in km/h (eg:- 200): ");
        double exitSpeedKmh = sc.nextDouble();

        System.out.print("Enable Push to pass? (true/false): ");
        boolean ptpOn = sc.nextBoolean();

        double lcv = exitSpeedKmh / 3.6;
        double ccp = 0.0;
        double ccv = exitSpeedKmh / 3.6;

        double time = 0.0;
        boolean overtaken = false;
        double overtakeMeter = 0.0;

        while (lcp < sln) {
            double gap = lcp - ccp;

            double leadDrag = 0.5 * rho * cd * area * (lcv * lcv);
            double leadEngine = Math.min(maxgrip, power / lcv);
            double leadAccel = (leadEngine - leadDrag) / mass;

            double chaseCd = cd;

            if (gap > 5.0 && gap < 100.0) {
                double slipstream = 0.05 * (1.0 - (gap / 100.0));
                chaseCd = chaseCd * (1.0 - slipstream);
            }

            double chasePower = power;
            if (ptpOn && gap < 100.0) {
                chasePower = power + 150000.0;
            }

            double chaseDrag = 0.5 * rho * chaseCd * area * (ccv * ccv);
            double chaseEngine = Math.min(maxgrip, chasePower / ccv);
            double chaseAccel = (chaseEngine - chaseDrag) / mass;

            lcv = lcv + (leadAccel * dt);
            lcp = lcp + (lcv * dt);

            ccv = ccv + (chaseAccel * dt);
            ccp = ccp + (ccv * dt);

            time = time + dt;

            if (overtaken == false && ccp > lcp) {
                overtaken = true;
                overtakeMeter = ccp;
            }
        }

        System.out.println("\nF1 PUSH TO PASS OVERTAKING SIMULATION RESULTS");
        System.out.println("Push to Pass Enabled: " + ptpOn);
        System.out.printf("Total Time on Straight: %.2f seconds\n", time);
        System.out.printf("Lead Car Top Speed: %.1f km/h\n", lcv * 3.6);
        System.out.printf("Chasing Car Top Speed: %.1f km/h\n", ccv * 3.6);

        if (overtaken) {
            System.out.printf("OVERTAKE SUCCESSFUL at %.1f meters!\n", overtakeMeter);
        } else {
            System.out.printf("OVERTAKE FAILED. Still %.1f meters behind.\n", (lcp - ccp));
        }
    }
}
