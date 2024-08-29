import { useNavigate } from "react-router";

import { Layout } from "../components/common/Layout";
import { Success } from "../components/common/Success";
import { Blob_1 } from "../components/common/Background/Blob_1";
import { Button } from "../components/common/Button";

const SignupSuccess = () => {
  const navigate = useNavigate();
  return (
    <Layout>
      <div className="relative">
        <div className="absolute right-[-162px] top-[-212px] animate-moveBlob2">
          <Blob_1 />
        </div>
      </div>
      <div className="min-h-100 mt-20 flex flex-col">
        <h1 className="mb-24 ml-8 mt-8 text-3xl font-bold">회원가입</h1>
        <Success />
        <div className="mb-4 mt-24 text-center">
          <h2 className="mb-2 text-2xl font-bold text-gray-600">가입 성공</h2>
          <p>
            로그인하여
            <br />
            서비스를 이용해보세요.
          </p>
        </div>
      </div>
      <div className="mx-4 mb-4 mt-8 flex flex-col gap-2">
        <Button content="확인" onClick={() => navigate("/home")} />
      </div>
    </Layout>
  );
};

export default SignupSuccess;
