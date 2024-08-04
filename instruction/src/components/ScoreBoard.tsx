import Text from "@/components/Text";

const ScoreBoard = ({
  username,
  score,
}: {
  username: string;
  score: number;
}) => {
  return (
    <div className="min-w-[250px] w-fit bg-lightdark flex flex-col gap-[15px] py-[30px] rounded-[6px]">
      <div className="w-full flex flex-col gap-[10px] items-center">
        <Text type="white" size="xs">
          Team Name
        </Text>
        <Text type="white" size="sub-banner" className="mt-[-5px]">
          {username}
        </Text>
      </div>
      <div className="w-full flex flex-col gap-[10px] items-center">
        <Text type="white" size="xs">
          Score
        </Text>
        <div className="w-[80px] h-[80px] flex justify-center items-center rounded-[6px] bg-primary">
          <Text type="white" size="sub-banner">
            {score}
          </Text>
        </div>
      </div>
    </div>
  );
};

export default ScoreBoard;
