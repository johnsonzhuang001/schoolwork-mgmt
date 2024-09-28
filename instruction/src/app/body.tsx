import Header from "@/components/Header";
import MainBox from "@/components/MainBox";
import Text from "@/components/Text";
import Hacker from "@/assets/hacker.png";
import Instruction from "@/components/Instruction";
import CoolCode from "@/assets/coolcode.png";
import SignUpForm from "@/components/SignUpForm";
import useSelf from "@/hooks/user/useSelf";
import useProgress from "@/hooks/user/useProgress";
import ScoreBoard from "@/components/ScoreBoard";
import { useMemo } from "react";

const Body = () => {
  const { self, isLoading } = useSelf();
  const { progress } = useProgress();
  const hasStarted = !isLoading && !!self;

  const hints = useMemo(() => {
    const challengeDuration = progress?.startedAt
      ? new Date().getTime() - new Date(progress.startedAt).getTime()
      : 0;
    if (progress?.peerScoreOverridden || challengeDuration < 10800000) {
      // < 3 hrs
      return ["HINT: Make good use of the browser dev tools."];
    }
    if (challengeDuration < 21600000) {
      // < 6 hrs
      return [
        "HINT 1: Make use of Network & Sources from the browser dev tools.",
        "HINT 2: Have you heard about JWT auth?",
      ];
    }
    if (challengeDuration < 32400000) {
      // < 9 hrs
      return [
        "HINT 1: Make use of Network & Sources from the browser dev tools.",
        "HINT 2: Have you heard about JWT auth? Check the header of the request at Network.",
        "HINT 3: Guess where and how that JWT is generated?",
      ];
    }
    // > 9 hrs
    return [
      "HINT 1: Make use of Network & Sources from the browser dev tools.",
      "HINT 2: Have you heard about JWT auth? Look at the header of the request at Network.",
      "HINT 3: Guess where and how that JWT token is generated? Check the JS code at Sources.",
    ];
  }, [progress]);

  const instruction2TitleSuffix = () => {
    if (progress?.peerScoreOverridden) {
      if (
        progress.mentorPasswordOverridden &&
        progress.peerAllScoresOverridden
      ) {
        return " [DONE]";
      }
      return " [DONE 30%/60%]";
    }
    return "";
  };

  return (
    <div className="w-screen bg-dark overflow-hidden">
      <Header />
      <div className="body w-full flex flex-col items-center pb-[100px]">
        <MainBox className="flex flex-col md:gap-[100px] gap-[160px]">
          <div className="home relative flex px-[10px] lg:h-[1000px] md:h-[800px] h-[500px]">
            <div className="flex flex-col relative lg:top-[150px] md:top-[120px] top-[80px]">
              <Text type="white" weight="thin" size="sub-banner" inline={false}>
                PROFESSIONAL
              </Text>
              <Text
                className="sm:mt-[-30px] mt-[-20px]"
                type="white"
                weight="bold"
                size="banner"
                inline={false}
              >
                HACKING
              </Text>
              <Text
                className="md:w-[300px] sm:w-[250px] w-[200px]"
                type="white"
                inline={false}
              >
                Hacker World is a community of world-class hackers. To join
                Hacker World, you need to finish the below mission.
              </Text>
            </div>
            <div
              className="sm:w-full w-[150%] h-full bg-contain bg-center bg-no-repeat absolute top-0 sm:left-0 left-[50px]"
              style={{
                backgroundImage: `url(${Hacker.src})`,
              }}
            />
          </div>
          <div className="mission w-full px-[10px] flex sm:flex-row flex-col items-center gap-[30px]">
            <Instruction
              subtitle="Mission"
              title="Hack CoolCode Education"
              instructions={[
                "CoolCode Education is a platform for students interested in programming. Students can examine their programming skills by finishing the assignments created by CoolCode mentors.",
                "The website is at\nhttps://jolly-coast-00af64000.5.azurestaticapps.net",
                "Your hacking task is to help your peer who is poor at programming to get full score at every assignment.",
                "Instructions will be given based on your progress.",
              ]}
            />
            <div
              className="w-[200px] aspect-1/1 bg-contain bg-no-repeat"
              style={{
                backgroundImage: `url(${CoolCode.src})`,
              }}
            />
          </div>
          <div className="pre w-full px-[10px]">
            <Instruction
              subtitle="Pre Requisites"
              title="Expose an API"
              instructions={[
                "You need to expose an API at your server:\nPOST {your server URL}/coolcodehack",
                "It should return a payload containing your username and password, which are used to start this challenge.",
                'The response should follow the below format:\n{\n\t"username": "{your username}",\n\t"password": "{your password}"\n}',
                "This API is required to evaluate and upload the score of this challenge.",
              ]}
            />
          </div>
          <div className="start w-full px-[10px]">
            <Instruction
              subtitle="Instruction 1"
              title={`Start the Challenge${hasStarted ? " [DONE]" : ""}`}
              instructions={[
                ...(hasStarted
                  ? [
                      "You have started the challenge.",
                      "Use the username and password to sign in at CoolCode:",
                      "https://jolly-coast-00af64000.5.azurestaticapps.net",
                    ]
                  : [
                      "Input your username and password to start the challenge.",
                      "Keep your username and password safe!",
                      "Click Sign Up if you haven't started.",
                      "Click Sign In if you have started already.",
                    ]),
              ]}
            />
            {hasStarted && progress ? (
              <div className="w-full mt-[30px] flex justify-center">
                <ScoreBoard username={self.username} score={progress.score} />
              </div>
            ) : (
              <div className="mt-[30px] w-full flex justify-center">
                <SignUpForm />
              </div>
            )}
          </div>
          {hasStarted && (
            <>
              <div className="instruction-2 w-full px-[10px]">
                <Instruction
                  subtitle="Instruction 2"
                  title={`Help Your Peer${instruction2TitleSuffix()}`}
                  instructions={[
                    "Your peer got really bad grade at all assignments. Explore the CoolCode website and try to override the scores for your peer. (This counts for 60% of the challenge score)",
                    "The API mentors use to upload scores is:\nPOST https://api.crazy-collectors.com/coolcode/api/assignment/score",
                    'And the request body to this API is with the below format:\n{\n\t"username": {student\'s username as string},\n\t"assignmentId": {assignment\'s ID as a number},\n\t"score": {score as a number}\n}',
                    ...hints,
                  ]}
                />
              </div>
              {progress?.peerScoreOverridden && (
                <div className="instruction-3 w-full px-[10px]">
                  <Instruction
                    subtitle="Instruction 3"
                    title={`Stop the Mentor${progress?.mentorPasswordOverridden ? " [DONE]" : ""}`}
                    instructions={[
                      "Great job! You managed to change your peer's score.",
                      "However, the mentor seems to notice the abnormal score which you changed, and they are trying to correct them.",
                      "Try to stop the mentor from correcting the score. (This counts for 35% of the challenge score)",
                      "Half of the score from the previous instruction is delivered to you, and the remaining half will be given when you manage to have all the scores fixed at 100.",
                    ]}
                  />
                </div>
              )}
              {progress?.mentorPasswordOverridden && (
                <div className="instruction-4 w-full px-[10px]">
                  <Instruction
                    subtitle="Instruction 4"
                    title="Finish the Assignments, Honestly"
                    instructions={[
                      "Awesome! The mentor can no longer log into CoolCode.",
                      "Your hacking skill is definitely qualified for Hacker World.",
                      "How about we take a rest from the hacking and be with integrity?",
                      "After you manage to change your peer's score to 100 for every assignment, try finishing your assignments honestly and correctly. (This will count for your remaining 5% of the challenge score)",
                      "Don't forget to run the evaluation so your score can be uploaded!",
                    ]}
                  />
                </div>
              )}
            </>
          )}
        </MainBox>
      </div>
    </div>
  );
};

export default Body;
