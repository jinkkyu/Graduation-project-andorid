package com.example.paparazzi.paparazzi_planer.Helper_Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;



public class DataLoader {

    private ArrayList<Contact> datas = new ArrayList<>();
    private Context context;


    public DataLoader(Context context) {
        this.context = context;
    }

    public ArrayList<Contact> get() {
        return datas;
    }

    public void load() {
        //1. 주소록에 접근하기 위해 ContentResolver를 불러오고
        ContentResolver resolver = context.getContentResolver();
        //2. 주소록에서 가져올 데이터 컬럼명을 정의한다
        String projections[] = {ContactsContract.CommonDataKinds.Phone.CONTACT_ID, //data id
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, // name
                ContactsContract.CommonDataKinds.Phone.NUMBER}; // phone number
        //3. ContentResolver로 쿼리한 데이터를 커서에 담는다.
        //전화번호 uri : ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        //주소록 uri : ContactsContract.Contacts.CONTENTS_URI
        //주소록 uri 일때는 전화번호가 있는지 확인하는 상수 HAS_PHONE_NUMBER > cursors 속도가 느려질 수 있다.
       Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI //데이터의 주소
        , projections //가져올 데이터 column명 배열
                , null // 조건절에 들어가는 컬럼명들 지정
                , null //지정된 컬럼명과 매핑되는 실제 조건 값
                , null //정렬
        );
        if(cursor !=null) {
            //4. 커서에 넘어온 데이터가 있다면 반복문을 돌면서 datas에 담아준다.
            while(cursor.moveToNext()) {
                Contact contact = new Contact();
                //5. 커서의 컬럼 인덱스를 가져온 후
              int idx =  cursor.getColumnIndex(projections[0]);
                //5.1
                contact.setId(cursor.getInt(idx));
                idx =  cursor.getColumnIndex(projections[1]);
                contact.setName(cursor.getString(idx));
                idx =  cursor.getColumnIndex(projections[2]);
                contact.addNumber(cursor.getString(idx));

                datas.add(contact);
            }
            cursor.close(); //사용 후 close를 호출하지 않으면 메모리 누수가 발생할 수 있다.
        }

    }
}
