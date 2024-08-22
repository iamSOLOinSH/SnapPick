import { useState } from "react";

import { Layout } from "../components/common/Layout";
import { Button } from "../components/common/Button";
import { Input } from "../components/common/Input";
import { Card } from "../components/common/Card";
import { NumberSelector } from "../components/common/NumberSelector";
import { ProgressSteps } from "../components/common/ProgressSteps";
import { TicketCard } from "../components/common/TickedCard";
import { Success } from "../components/common/Success";

import { BsQuestionCircle } from "react-icons/bs";

import { useBoundStore } from "../store/store";

const Main = () => {
  const { stores, getAllStores, user, login } = useBoundStore((state) => ({
    stores: state.stores,
    getAllStores: state.getAllStores,
    user: state.user,
    login: state.login,
  }));

  const [quantity, setQuantity] = useState(3);

  const handleIncrease = () => setQuantity(quantity + 1);
  const handleDecrease = () => setQuantity(quantity - 1);
  return (
    <Layout>
      <div className="mt-36" />
      <Success />
      <div className="mb-36" />
      <Button variant="primary" content="다음" />
      <div className="my-2" />
      <Button variant="secondary" content="취소" />
      <div className="my-2" />
      <Input variant="full" placeholder="스토어 이름(최대 50자)" />
      <div className="my-2" />
      <div>
        <Input variant="half" placeholder="수량" /> <span> </span>
        <Input variant="half" placeholder="수량" />
      </div>
      <div className="my-2" />
      <div>
        <Input variant="third" placeholder="010" /> <span> - </span>
        <Input variant="third" placeholder="010" /> <span> - </span>
        <Input variant="third" placeholder="010" />
      </div>
      <div className="my-2" />
      <div>
        <span>월 </span>
        <Input variant="twoThirds" placeholder="09:00 - 18:00" />
      </div>
      <div className="my-2" />
      <div className="align-center flex">
        <Input variant="check" type="checkbox" />
        <span className="ml-2">해당 없음</span>
      </div>
      <div className="my-2" />
      <Card
        variant="status"
        title="상태"
        content={
          <div className="flex items-center gap-4">
            <div className="h-3 w-3 rounded-full bg-red-500"></div>
            <span>품절</span>
            <BsQuestionCircle />
          </div>
        }
      />
      <Card variant="status" title="옵션" content={<div>상품 옵션 1</div>} />
      <Card
        variant="store"
        title="팝업 스토어 1"
        subtitle="팝업스토어 소개"
        description="운영시간 10:30-20:00"
        imageSrc="https://search.pstatic.net/sunny?src=https%3A%2F%2Fi.namu.wiki%2Fi%2F8XSPz74OmwKAlPxupaSpYLQXgHG86E1drwvqaeNB0LnxJ6Vz73iPKe4C2xlkLNBY18QVXJi4PaZYv8rusG_9bQ.webp&type=fff208_208"
      />
      <Card
        variant="product"
        title="스토어 상품 1"
        subtitle="개당 ￦16,000"
        price={`${quantity * 16000}원`}
        imageSrc="https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300"
        toggle={
          <NumberSelector
            quantity={quantity}
            onIncrease={handleIncrease}
            onDecrease={handleDecrease}
          />
        }
      />
      <Card
        variant="simple"
        title="팝업 스토어 1"
        date="2024년 8월 17일"
        time="15:02"
        imageSrc="https://search.pstatic.net/sunny?src=https%3A%2F%2Fi.namu.wiki%2Fi%2F8XSPz74OmwKAlPxupaSpYLQXgHG86E1drwvqaeNB0LnxJ6Vz73iPKe4C2xlkLNBY18QVXJi4PaZYv8rusG_9bQ.webp&type=fff208_208"
        spend="-156,000원"
      />
      <Card
        variant="simple"
        title="팝업 스토어 1"
        date="2024년 8월 17일"
        time="15:02"
        imageSrc="https://search.pstatic.net/sunny?src=https%3A%2F%2Fi.namu.wiki%2Fi%2F8XSPz74OmwKAlPxupaSpYLQXgHG86E1drwvqaeNB0LnxJ6Vz73iPKe4C2xlkLNBY18QVXJi4PaZYv8rusG_9bQ.webp&type=fff208_208"
      />
      <div className="my-2" />
      <ProgressSteps currentStep={1} steps={3} />
      <div className="my-2" />
      <ProgressSteps currentStep={2} steps={3} />
      <div className="my-2" />
      <ProgressSteps currentStep={3} steps={3} />
      <div className="my-2" />
      <TicketCard
        title="팝업스토어 1"
        date="오늘"
        location="팝업스토어 위치"
        seller="판매자"
        buttonText="상세보기"
        isActive={true}
      />
      <div className="my-2" />
      <TicketCard
        title="팝업스토어 2"
        date="2024년 7월 30일"
        location="팝업스토어 위치"
        seller="판매자"
        buttonText="상세보기"
        isActive={false}
      />
      <div>{stores[0].name}</div>
      <div className="my-2" />
      <div className="max-w-md gap-1 overflow-auto whitespace-nowrap">
        <Card
          variant="mini"
          title="인기 팝업 스토어 2asdasdasdas"
          imageSrc="https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300" // 이미지 URL을 여기에 추가하세요.
        />
        <Card
          variant="mini"
          title="인기 팝업 스토어 2asdasdasdas"
          imageSrc="https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300" // 이미지 URL을 여기에 추가하세요.
        />
        <Card
          variant="mini"
          title="인기 팝업 스토어 2asdasdasdas"
          imageSrc="https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300" // 이미지 URL을 여기에 추가하세요.
        />
        <Card
          variant="mini"
          title="인기 팝업 스토어 2asdasdasdas"
          imageSrc="https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300" // 이미지 URL을 여기에 추가하세요.
        />
        <Card
          variant="mini"
          title="인기 팝업 스토어 2asdasdasdas"
          imageSrc="https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300"
        />
      </div>
    </Layout>
  );
};

export default Main;
