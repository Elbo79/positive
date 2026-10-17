import 'package:flutter/material.dart';

class FamilyCompassScreen extends StatefulWidget {
  final bool forPartner;
  const FamilyCompassScreen({super.key, required this.forPartner});

  @override
  State<FamilyCompassScreen> createState() => _FamilyCompassScreenState();
}

class _FamilyCompassScreenState extends State<FamilyCompassScreen> {
  final Set<int> completed = {};
  final TextEditingController reflection = TextEditingController();

  @override
  void dispose() {
    reflection.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final partner = widget.forPartner;
    final steps = partner
        ? <(String, String)>[
            ('Listen before evaluating', 'Tell me what happened. I am listening.'),
            ('Connect before fixing', 'That sounds difficult. I am glad you told me.'),
            ('Ask what support is wanted', 'Would you like me to listen, brainstorm, or share my opinion?'),
            ('Repair when needed', 'I jumped into criticism. I am sorry. Can we start again?'),
          ]
        : <(String, String)>[
            ('Connect without correcting', 'Spend time together with no coaching or evaluation.'),
            ('Notice genuine effort', 'I saw you chase that ball. How did that feel?'),
            ('Ask, do not assume', 'What do you enjoy about soccer? Anything make it stressful?'),
            ('Value courage over approval', 'Recognize honesty, kindness, trying, and learning.'),
          ];
    return Scaffold(
      appBar: AppBar(title: Text(partner ? 'Be a Safe Place' : 'Build Confidence')),
      body: SafeArea(
        child: ListView(
          padding: const EdgeInsets.all(20),
          children: [
            Text(
              partner
                  ? 'Make it easier for your partner to bring you difficult things.'
                  : 'Help your child feel valued without having to impress anyone.',
              style: Theme.of(context).textTheme.titleLarge,
            ),
            const SizedBox(height: 12),
            Text(partner
                ? 'Support does not require agreement. Listen first, then decide together whether advice is welcome.'
                : 'Low effort can have many causes. Stay curious about what your child feels and wants.'),
            const SizedBox(height: 20),
            for (var i = 0; i < steps.length; i++)
              Card(
                child: CheckboxListTile(
                  value: completed.contains(i),
                  onChanged: (checked) => setState(() {
                    if (checked == true) {
                      completed.add(i);
                    } else {
                      completed.remove(i);
                    }
                  }),
                  title: Text(steps[i].$1),
                  subtitle: Padding(
                    padding: const EdgeInsets.only(top: 6),
                    child: Text(steps[i].$2),
                  ),
                  controlAffinity: ListTileControlAffinity.leading,
                  isThreeLine: true,
                ),
              ),
            const SizedBox(height: 16),
            Text('${completed.length} of ${steps.length} practiced today'),
            const SizedBox(height: 16),
            TextField(
              controller: reflection,
              maxLines: 3,
              decoration: const InputDecoration(
                labelText: 'What did I notice?',
                hintText: 'A small moment I want to remember...',
                border: OutlineInputBorder(),
                alignLabelWithHint: true,
              ),
            ),
            const SizedBox(height: 8),
            const Text('This reflection is not saved after you leave this screen.'),
            const SizedBox(height: 20),
            OutlinedButton.icon(
              onPressed: () => setState(() {
                completed.clear();
                reflection.clear();
              }),
              icon: const Icon(Icons.refresh),
              label: const Text('Start a new practice'),
            ),
          ],
        ),
      ),
    );
  }
}
