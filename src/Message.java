import java.util.ArrayList;
import java.util.List;

public class Message
{
    private Headers m_Headers;
    private ArrayList<Question> m_QuestionsList;
    private ArrayList<ResourceRecord> m_AnswersList;
    private ArrayList<ResourceRecord> m_AuthorityList;
    private ArrayList<ResourceRecord> m_AdditionalsList;
    private byte[] m_Content;
    private int m_End;

    public Message(byte[] i_Content)
    {
        this.m_Content = i_Content;
        this.m_QuestionsList = new ArrayList<>();
        this.m_AnswersList = new ArrayList<>();
        this.m_AuthorityList = new ArrayList<>();
        this.m_AdditionalsList = new ArrayList<>();
        this.m_Headers = new Headers(this.m_Content);

        int currentStart = 12;
        Question question;
        for (int i = 0; i < this.m_Headers.getQDCOUNT(); i++)
        {
            question = new Question(this.m_Content, currentStart);
            this.m_QuestionsList.add(question);
            currentStart = question.getEnd();
        }

        ResourceRecord answer;
        for(int i = 0; i < this.m_Headers.getANCOUNT(); i++)
        {
            answer = new ResourceRecord(eType.Answer, this.m_Content, currentStart);
            this.m_AnswersList.add(answer);
            currentStart = answer.getEnd();
        }

        ResourceRecord authority;
        for(int i = 0; i < this.m_Headers.getNSCOUNT(); i++)
        {
            authority = new ResourceRecord(eType.Authority, this.m_Content, currentStart);
            this.m_AuthorityList.add(authority);
            currentStart = authority.getEnd();
        }

        ResourceRecord additional;
        for(int i = 0; i < this.m_Headers.getARCOUNT(); i++)
        {
            additional = new ResourceRecord(eType.Answer, this.m_Content, currentStart);
            this.m_AdditionalsList.add(additional);
            currentStart = additional.getEnd();
        }

        this.m_End = currentStart;
    }

    public Headers getHeaders()
    {
        return this.m_Headers;
    }

    public ArrayList<Question> getQuestionsList()
    {
        return this.m_QuestionsList;
    }

    public ArrayList<ResourceRecord> getAnswersList()
    {
        return this.m_AnswersList;
    }

    public ArrayList<ResourceRecord> getAuthorityList()
    {
        return this.m_AuthorityList;
    }

    public ArrayList<ResourceRecord> getAdditionalsList()
    {
        return this.m_AdditionalsList;
    }

    public byte[] getContent()
    {
        return this.m_Content;
    }

    public int getEnd() { return this.m_End; }

}
