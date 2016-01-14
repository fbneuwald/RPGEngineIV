package rpgengineiv.com.neuwaldlabs;

import java.util.Random;

/**
 * Created by fneuwald on 23/05/2014.
 */
public class AttackProcessor {
    public static void  processMonsterAttack(Monster Attacker, Character defender)
    {
        //TODO: Process an attack form a Monster to player
        Random rnd = new Random (System.nanoTime());
        boolean hit = false;
        int att, def;
        int damage;
        att = rnd.nextInt();
        if(att<0)
            att = att*-1;
        att = att %10;

        if(att == 0) //critical miss
            hit = false;
        else
            if(att==10) //critical hit
                hit= true;
            else {
                att = att + Attacker.GetDex();
                def = rnd.nextInt() % 10;
                if (def < 0)
                    def= def *-1;
                def = def + defender.GetArmor();
                if(att>def)
                    hit =true;
            }



        //TODO: calculate damage = attacker Weapon damage + str - defender cons
        if(hit)
        {
            damage = Attacker.GetStr() - defender.GetConstitution();
            if (damage < 0)
                damage = 1;
            defender.applyDamage(damage);

        }
        //TODO: apply Damage


    }
    public static void  processPlayerAttack(Character Attacker, Monster defender)
    {
        //TODO: Process an attack form a Monster to player
        Random rnd = new Random (System.nanoTime());
        boolean hit = false;
        int att, def;
        int damage;
        att = rnd.nextInt();
        if(att<0)
            att = att*-1;
        att = att %10;

        if(att == 0) //critical miss
            hit = false;
        else
        if(att==10) //critical hit
            hit= true;
        else {
            att = att + Attacker.GetDexterity();
            def = rnd.nextInt() % 10;
            if (def < 0)
                def= def *-1;
            def = def + defender.GetArmor();
            if(att>def)
                hit =true;
        }



        //TODO: calculate damage = attacker Weapon damage + str - defender cons
        if(hit)
        {
            damage = Attacker.GetStrength() - defender.GetCon();
            if (damage < 0)
                damage = 1;
            defender.takeDamage(damage);

        }
        //TODO: apply Damage
    }
}
