package com.entity.p;

import javax.persistence.*;

@Entity
@Table(name = "b2i_agent")
public class BtiAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="agent_id_SequenceGenerator")
    @SequenceGenerator(name="agent_id_SequenceGenerator", sequenceName="comm_seq")
    @Column(nullable = false)
    long id ;
    @Column()
    String tele;
    @Column()
    String name;
    @Column()
    String secretId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }


}
