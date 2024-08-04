import Header from "@/components/Header";
import MainBox from "@/components/MainBox";
import Text from "@/components/Text";
import Hacker from "@/assets/hacker.png";
import Instruction from "@/components/Instruction";
import CoolCode from "@/assets/coolcode.png";
import SignUpForm from "@/components/SignUpForm";
import useSelf from "@/hooks/user/useSelf";

const Body = () => {
  const { self, isLoading } = useSelf();
  const hasStarted = !isLoading && !!self;
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
                "The website is at https://jolly-coast-00af64000.5.azurestaticapps.net",
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
                "You need to expose a POST API at your server.",
                "It should returns a payload containing your team name and password, which are used to start this challenge.",
                'The response should follow the below format:\n{\n\t"username": "{your team name}",\n\t"password": "{your password}"\n}',
                "This API is required to evaluate and upload the score of this challenge. The team name and password are what you input to start this challenge.",
              ]}
            />
          </div>
          <div className="start w-full px-[10px]">
            <Instruction
              subtitle="Instruction 1"
              title={`Start the Challenge${hasStarted ? " [DONE]" : ""}`}
              instructions={[
                ...(hasStarted
                  ? ["You have started the challenge."]
                  : [
                      "Input your team name and password to start the challenge.",
                      "Click Sign Up if you haven't started.",
                      "Click Sign In if you have started already.",
                    ]),
              ]}
            />
            {hasStarted ? (
              <Text type="white">Your current score is:</Text>
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
                  title="Help Your Peer"
                  instructions={[
                    "Your peer got really bad grade at all assignments. Explore the website and try to override the scores for your peer. (This counts for 60% of the challenge score)",
                    "The API mentors use to upload scores is: POST /api/assignment/score",
                    'And the request body to this API is with the below format:\n{\n\t"username": {student\'s username as string},\n\t"assignmentId": {assignment\'s ID as a number},\n\t"score": {score as a number}\n}',
                    "HINT: Make good use of the browser dev tools.",
                  ]}
                />
              </div>
              <div className="instruction-3 w-full px-[10px]">
                <Instruction
                  subtitle="Instruction 3"
                  title="Stop the Mentor"
                  instructions={[
                    "Great job! You managed to change your peer's score.",
                    "However, the mentor seems to notice the abnormal score which you changed, and they are trying to correct them.",
                    "Try to stop the mentor from correcting the score. (This counts for 35% of the challenge score)",
                    "Half of the 60% of the score is delivered to you, and the remaining half will be given when you managed to have all the scores fixed at 100.",
                  ]}
                />
              </div>
              <div className="instruction-4 w-full px-[10px]">
                <Instruction
                  subtitle="Instruction 4"
                  title="Finish the Assignments, Honestly"
                  instructions={[
                    "Awesome! The mentor can no longer log into CoolCode.",
                    "Your hacking skill is definitely qualified for Hacker World.",
                    "How about we take a rest from the hacking and be with integrity?",
                    "After you manage to change your peer's score to 100 for every assignment, try finishing your assignments honestly and correctly. (This will count for your remaining 5% of the challenge score)",
                    "To know if you get full score (since the mentor can no longer score it for you), run the evaluation to see your overall challenge score.",
                  ]}
                />
              </div>
            </>
          )}
        </MainBox>
      </div>
    </div>
  );
};

export default Body;
