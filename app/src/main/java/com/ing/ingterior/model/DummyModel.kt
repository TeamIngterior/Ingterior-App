package com.ing.ingterior.model

import com.ing.ingterior.Logging.logD
import com.ing.ingterior.R
import com.ing.ingterior.model.MessageModel.Companion.TYPE_RECEIVE
import com.ing.ingterior.model.MessageModel.Companion.TYPE_SEND
import com.ing.ingterior.model.MessageModel.Companion.canClusterWithMessage
import com.ing.ingterior.model.MessageModel.Companion.showDateWithMessage
import java.util.Date

object DummyModel {
    private const val TAG = "DummyModel"

    const val SECOND:Long = 1000L
    const val MINUTE:Long = 60 * SECOND
    const val HOUR:Long = 60 * MINUTE
    const val DAY:Long = 24 * HOUR

    private const val oneDayMillis: Long = DAY

    fun getDummySchedule(): ArrayList<EventModel>{
        val schedules = arrayListOf<EventModel>()
        schedules.add(
            EventModel(1, "가나다라마바사아자차카타파하가나다라마바사아자차카타파하", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis, System.currentTimeMillis(), R.color.palette1))
        schedules.add(
            EventModel(2, "가나다라마바사아자차카타파하가나다라마바사아자차카타파하", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis(), System.currentTimeMillis()+oneDayMillis*2, R.color.palette10))
        schedules.add(
            EventModel(3, "테스트", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis(), System.currentTimeMillis()+oneDayMillis*3, R.color.palette3))
        schedules.add(
            EventModel(4, "테스트2", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis(), System.currentTimeMillis(), R.color.palette4))
        schedules.add(
            EventModel(5, "테스트3", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis*2, System.currentTimeMillis()-oneDayMillis, R.color.palette5))
        schedules.add(
            EventModel(6, "테스트4", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis*4, System.currentTimeMillis()-oneDayMillis*3, R.color.palette9))
        schedules.add(
            EventModel(7, "테스트5", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis*2, System.currentTimeMillis()-oneDayMillis, R.color.palette8))
        schedules.add(
            EventModel(8, "테스트6", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()-oneDayMillis*2, System.currentTimeMillis()-oneDayMillis*2, R.color.palette8))
        schedules.add(
            EventModel(9, "테스트7", "테스트입니다.테스트입니다.테스트입니다.테스트입니다.테스트입니다.",
                System.currentTimeMillis()+oneDayMillis*2, System.currentTimeMillis()+oneDayMillis*10, R.color.palette8))
        return schedules
    }

    fun getConversationDummyList() : ArrayList<ConversationModel>{
        val arrayList = arrayListOf<ConversationModel>()
        arrayList.add(ConversationModel(1,   "1", "가나다라마바사아자차카타파하가나다라마바사아자차카타파하","내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용내용",
            System.currentTimeMillis(), 2))
        arrayList.add(ConversationModel(2,  "1 2", "테스트2","내용내용내용내용내용내용",
            System.currentTimeMillis(), 2))
        arrayList.add(ConversationModel(3,   "1 2 3 4", "테스트3","내용내용내용내용",
            System.currentTimeMillis(), 2))
        arrayList.add(ConversationModel(3,   "1 2 4", "테스트3","내용내용내용내용내용내용내용내용내용내용내용내용내용내용",
            System.currentTimeMillis(), 0))
        return arrayList
    }

    fun getMessageDummyList(): ArrayList<MessageModel> {
        val arrayList = arrayListOf<MessageModel>()
        val currentTime = Date().time
        arrayList.add(MessageModel(1, 1, "테스터1", "법원은 최고법원인 대법원과 각급법원으로 조직된다. ", currentTime - DAY - 2 * MINUTE - 30 * SECOND,
            TYPE_SEND, 1
        ))
        arrayList.add(MessageModel(2, 1, "테스터1", "국회는 국민의 보통·평등·직접·비밀선거에 의하여 선출된 국회의원으로 구성한다. 국가는 농수산물의 수급균형과 유통구조의 개선에 노력하여 가격안정을 도모함으로써 농·어민의 이익을 보호한다.", currentTime - DAY  - 2*MINUTE - 20*SECOND,
            TYPE_SEND, 1
        ))

        arrayList.add(MessageModel(3, 1, "테스터1", "테스트",
            currentTime - DAY - 2 * MINUTE - 3 * SECOND,
            TYPE_RECEIVE, 1
        ))

        arrayList.add(MessageModel(4, 1, "테스터1", "형사피고인은 유죄의 판결이 확정될 때까지는 무죄로 추정된다. 국가는 노인과 청소년의 복지향상을 위한 정책을 실시할 의무를 진다. 명령·규칙 또는 처분이 헌법이나 법률에 위반되는 여부가 재판의 전제가 된 경우에는 대법원은 이를 최종적으로 심사할 권한을 가진다.",
            currentTime - DAY - 2 * MINUTE - 2 * SECOND,
            TYPE_RECEIVE, 1
        ))

        arrayList.add(MessageModel(5, 1, "테스터1", "  ",
            currentTime - DAY - 2 * MINUTE - SECOND,
            TYPE_RECEIVE, 1
        ))

        arrayList.add(MessageModel(6, 1, "테스터1", "사면·감형 및 복권에 관한 사항은 법률로 정한다. 타인의 범죄행위로 인하여 생명·신체에 대한 피해를 받은 국민은 법률이 정하는 바에 의하여 국가로부터 구조를 받을 수 있다. 사법권은 법관으로 구성된 법원에 속한다. 혼인과 가족생활은 개인의 존엄과 양성의 평등을 기초로 성립되고 유지되어야 하며, 국가는 이를 보장한다. 중앙선거관리위원회는 법령의 범위안에서 선거관리·국민투표관리 또는 정당사무에 관한 규칙을 제정할 수 있으며, 법률에 저촉되지 아니하는 범위안에서 내부규율에 관한 규칙을 제정할 수 있다. 국회는 국가의 예산안을 심의·확정한다. 모든 국민은 보건에 관하여 국가의 보호를 받는다.",
            currentTime - HOUR - 2 * MINUTE,
            TYPE_RECEIVE, 1
        ))

        arrayList.add(MessageModel(7, 1, "테스터1", "국무총리·국무위원 또는 정부위원은 국회나 그 위원회에 출석하여 국정처리상황을 보고하거나 의견을 진술하고 질문에 응답할 수 있다. 선거에 관한 경비는 법률이 정하는 경우를 제외하고는 정당 또는 후보자에게 부담시킬 수 없다. 이 헌법에 의한 최초의 대통령의 임기는 이 헌법시행일로부터 개시한다. 국회에서 의결된 법률안은 정부에 이송되어 15일 이내에 대통령이 공포한다. 모든 국민은 헌법과 법률이 정한 법관에 의하여 법률에 의한 재판을 받을 권리를 가진다.",
            currentTime - HOUR,
            TYPE_RECEIVE, 1
        ))

        arrayList.add(MessageModel(8, 1, "테스터1", "대법관의 임기는 6년으로 하며, 법률이 정하는 바에 의하여 연임할 수 있다.",
            currentTime,
            TYPE_SEND, 1
        ))
        return arrayList
    }

    fun getMessageList(): ArrayList<MessageModel> {
        val messageList = getMessageDummyList()
        for((i, message) in messageList.withIndex()) {
            if(messageList.size == 1) {
                message.isShowTime = true
                message.isShowAvatar = true
                message.isShowDate = true
            }
            else {
                logD(TAG, "getMessageList: i=$i")
                when (i) {
                    0 -> {
                        val nextMessage = messageList[1]
                        message.isShowTime = !canClusterWithMessage(message, nextMessage)
                        message.isShowAvatar = true
                        message.isShowDate = true
                    }
                    messageList.size-1 -> {
                        val prevMessage = messageList[i-1]
                        message.isShowAvatar = !canClusterWithMessage(prevMessage, message)
                        message.isShowTime = true
                        message.isShowDate = showDateWithMessage(prevMessage, message)
                    }
                    else -> {
                        val prevMessage = messageList[i-1]
                        val nextMessage = messageList[i+1]
                        message.isShowAvatar = !canClusterWithMessage(prevMessage, message)
                        message.isShowTime = !canClusterWithMessage(message, nextMessage)
                        message.isShowDate = showDateWithMessage(prevMessage, message)
                    }
                }
            }

        }
        return messageList
    }
}