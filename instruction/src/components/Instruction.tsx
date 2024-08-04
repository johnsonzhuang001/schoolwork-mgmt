import Text from "@/components/Text";
import React from "react";

interface InstructionProps {
  subtitle: string;
  title: string;
  instructions: string[];
}

const Instruction: React.FC<InstructionProps> = ({
  subtitle,
  title,
  instructions,
}) => {
  return (
    <div className="flex flex-col grow gap-[10px]">
      <div>
        <Text type="white" weight="thin" inline={false}>
          {subtitle}
        </Text>
        <Text
          className="sm:mt-[-10px]"
          type="white"
          size="sub-banner"
          weight="bold"
          inline={false}
        >
          {title}
        </Text>
      </div>
      <div className="max-w-[800px] flex flex-col gap-[5px]">
        {instructions.map((instruction, index) => {
          return (
            <Text key={index} type="white" wrap="prewrap" inline={false}>
              {instruction}
            </Text>
          );
        })}
      </div>
    </div>
  );
};

export default Instruction;
