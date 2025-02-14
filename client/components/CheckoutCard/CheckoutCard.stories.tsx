import React from "react";
import { ComponentStory, ComponentMeta } from "@storybook/react-native";
import { View } from "react-native";
import icon from "../../config/icon"
import { CheckoutCard } from "./CheckoutCard";

const MyButtonMeta: ComponentMeta<typeof CheckoutCard> = {
  title: "CheckoutCard",
  component: CheckoutCard,
  decorators: [
    (Story) => (
      <View style={{ padding: 32 }}>
        <Story />
      </View>
    ),
  ],
};

export default MyButtonMeta;

type CheckoutCardStory = ComponentStory<typeof CheckoutCard>;

export const BasicCheckoutCard: CheckoutCardStory = () => <CheckoutCard   />;

