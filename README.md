# JavaFinalExam

2018년도 고급자바프로그래밍 기말고사

1661030 엄선용

소비자의 소비 패턴을 구매빈도와 구매비용평균을 기준으로 Kmeans를 이용하여 군집화 시키는 프로그램 입니다.

중간고사

메인: \src\kr\dja\javaMidtermExam\Core.java

 - 소비자의 정보를 사용자가 직접 입력하거나 랜덤으로 입력
 - Kmeans군집 

기말고사(중간고사 프로젝트에 의존적)

메인: \src\kr\dja\javaFinalExam\ui\UIMain.java

 - 소비자 정보가 담긴 CSV파일을 읽어서 저장함
 - 저장된 자료를 kmeans로 분석함
 - kmeans 그룹에 별칭 부여(VIP, 자주구매, 고가구매, 일반고객)
 - 소비자의 각종 정보를 표시 가능
