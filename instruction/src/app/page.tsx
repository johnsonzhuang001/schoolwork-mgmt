import Header from "@/components/Header";
import MainBox from "@/components/MainBox";
import Text from "@/components/Text";
import Hacker from "@/assets/hacker.png";
import CoolCode from "@/assets/coolcode.png";

export default function Home() {
  return (
    <div className="w-screen bg-dark overflow-hidden">
      <Header />
      <div className="body w-full flex flex-col items-center">
        <MainBox className="flex flex-col md:gap-[30px] gap-[160px]">
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
          <div className="mission w-full px-[10px] relative flex sm:flex-row flex-col items-center gap-[30px]">
            <div className="flex flex-col grow gap-[10px]">
              <div>
                <Text type="white" weight="thin" inline={false}>
                  Mission
                </Text>
                <Text
                  className="sm:mt-[-10px]"
                  type="white"
                  size="sub-banner"
                  weight="bold"
                  inline={false}
                >
                  Hack CoolCode Education
                </Text>
              </div>
              <div className="max-w-[800px] flex flex-col gap-[5px]">
                <Text type="white" inline={false}>
                  CoolCode Education is a platform for students interested in
                  programming. Students can examine their programming skills by
                  finishing the assignments created by CoolCode mentors.
                </Text>
                <Text type="white" inline={false}>
                  Your hacking task is to help your peer who is poor at
                  programming to get full score at every assignment.
                </Text>
                <Text type="white" inline={false}>
                  Instructions will be given based on your progress.
                </Text>
              </div>
            </div>
            <div
              className="w-[200px] aspect-1/1 bg-contain bg-no-repeat"
              style={{
                backgroundImage: `url(${CoolCode.src})`,
              }}
            />
          </div>
        </MainBox>
      </div>
    </div>
  );
}
