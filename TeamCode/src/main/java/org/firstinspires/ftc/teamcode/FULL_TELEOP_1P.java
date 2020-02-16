//Imports:
package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


/*
7890 Space Lions 2019 "FULL TELEOP"
author: 7890 Software (TEAM MEMBERS)
GOALS: (GOALS)
 */
@TeleOp(name="FULL TELEOP 1P", group="Tele Op")
public class FULL_TELEOP_1P extends OpMode {
    private ElapsedTime runtime = new ElapsedTime();

    /*
    ---MOTORS---
     */
    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;
    DcMotor liftMotor;
    DcMotor intakeMotor;
    DcMotor lockMotor;
    Servo lock;
    DcMotor armMotor;

    /*
    ---DIRECTION SETUP---
     */
    private DcMotor.Direction LEFTDIRECTION = DcMotor.Direction.REVERSE;
    private DcMotor.Direction RIGHTDIRECTION = DcMotor.Direction.FORWARD;

    int spin = 0;

    @Override
    public void init() {

        /*
        ---HARDWARE MAP---
        */
        leftFront = hardwareMap.dcMotor.get("left front");
        leftBack = hardwareMap.dcMotor.get("left back");
        rightFront = hardwareMap.dcMotor.get("right front");
        rightBack = hardwareMap.dcMotor.get("right back");
        liftMotor = hardwareMap.dcMotor.get("lift motor");
        intakeMotor = hardwareMap.dcMotor.get("intake motor");
        armMotor = hardwareMap.dcMotor.get("arm motor");

        /*
        ---DIRECTIONS---
         */
        leftFront.setDirection(LEFTDIRECTION);
        leftBack.setDirection(LEFTDIRECTION);
        rightFront.setDirection(RIGHTDIRECTION);
        rightBack.setDirection(RIGHTDIRECTION);

    }

    @Override
    public void loop() {

        /*
        ---DRIVETRAIN---
         */
        float drive;
        float turn;
        float strafe;

        /*
        ---DRIVE CONTROLLER SETUP---
         */
        drive = -gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;
        strafe = -gamepad1.left_stick_x;


        /*
        ---MECANUM DRIVING CALCULATIONS---
         */
        //We calculate the power needed to set to each wheel by using a variable for drive,
        //turn, and strafe. By adding and subtracting these three variables against each
        //other we can calculate the power needed for each wheel in every situation.

        double lfDrive = Range.clip(drive + turn - strafe, -1.0, 1.0);
        double lbDrive = Range.clip(drive + turn + strafe, -1.0, 1.0);
        double rfDrive = Range.clip(drive - turn + strafe, -1.0, 1.0);
        double rbDrive = Range.clip(drive - turn - strafe, -1.0, 1.0);

        /*
        ---WHEEL POWERS---
         */
        //We set the power to wheels based off the values calculated above. We can move
        //in all directions based off of the combinations inputted by the driver.
        //We also added a "slow driving" function, allowing the driver to halve the speed
        //when the right bumper is pressed. This allows for more precise controls when the driver
        //needs to pick up or place a block, but also allows the robot to quickly move across the
        //field
        if(gamepad1.right_bumper){
            leftFront.setPower(lfDrive/2);
            leftBack.setPower(lbDrive/2);
            rightFront.setPower(rfDrive/2);
            rightBack.setPower(rbDrive/2);
        }else{
            leftFront.setPower(lfDrive);
            leftBack.setPower(lbDrive);
            rightFront.setPower(rfDrive);
            rightBack.setPower(rbDrive);
        }


        /*
        ---LIFT CONTROLLER SETUP---
         */
        //The lift controls are on the trigger so that the driver can both move and
        //raise + lower the lift at the same time. This allows for a more efficient tele-op.
        if(gamepad1.left_bumper) {
            if (gamepad1.right_trigger > 0.0) {
                liftMotor.setPower(0.5);
            }
            if (gamepad1.left_trigger > 0.0) {
                liftMotor.setPower(-0.5);
            }
            if (gamepad1.right_trigger <= 0.0 && gamepad1.left_trigger <= 0.0) {
                liftMotor.setPower(0.0);
            }
        }
        else {
            if (gamepad1.right_trigger > 0.0) {
                liftMotor.setPower(1.0);
            }
            if (gamepad1.left_trigger > 0.0) {
                liftMotor.setPower(-1.0); // hello erinaq
            }
            if (gamepad1.right_trigger <= 0.0 && gamepad1.left_trigger <= 0.0) {
                liftMotor.setPower(0.0);
            }
        }

        /*
        ---INTAKE WHEELS---
         */
        //These intake wheels are what we use to suck the stones up into our intake
        //mechanism. You press the button once to activate the wheels and then press
        //the button again to switch the direction of the wheels. You press the bumper
        //to stop the wheels.
        /*
        if (gamepad1.x && spin == 0) // in
        {
            gamepad1.x = false;
            intakeMotor.setPower(1);
            spin ^= 1;
            telemetry.addLine(spin + "");
            telemetry.update();
        }
        if (gamepad1.x && spin == 1)// stops intaking
        {
            gamepad1.x = false;
            intakeMotor.setPower(0);
            spin ^= 1;
            telemetry.addLine(spin+"");
            telemetry.update();
        }
        if (gamepad1.y) { // outtakes
            intakeMotor.setPower(-1);
            spin = 0;
            telemetry.addLine(spin+"");
            telemetry.update();
        }*/
        if(gamepad1.x) intakeMotor.setPower(1);
        if(gamepad1.y) intakeMotor.setPower(-1);
        if(gamepad1.b) intakeMotor.setPower(0);




        /*
        ---CONTROLLING THE ARM---
         */
        //This code gives us manual control the arm that we use to pull
        //the foundation. This means that we are able to reposition the tray during
        //tele-op if needed and during endgame.
        if(gamepad1.dpad_up)
            armMotor.setPower(-0.7);

        if(gamepad1.dpad_down)
            armMotor.setPower(0.7);

    }
}
