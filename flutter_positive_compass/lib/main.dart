import 'package:flutter/material.dart';
import 'features/practices/family_compass_screen.dart';

void main() => runApp(const PositiveCompassApp());

class PositiveCompassApp extends StatelessWidget {
  const PositiveCompassApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Positive Compass',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: const Color(0xFF41657A)),
        useMaterial3: true,
      ),
      home: const CompassHome(),
    );
  }
}

class CompassHome extends StatelessWidget {
  const CompassHome({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Positive Compass')),
      body: SafeArea(
        child: ListView(
          padding: const EdgeInsets.all(24),
          children: [
            Text('I coach before I criticize.',
                style: Theme.of(context).textTheme.headlineMedium),
            const SizedBox(height: 8),
            const Text('Helping you become the person you choose to be.'),
            const SizedBox(height: 24),
            Card(
              child: ListTile(
                leading: const Icon(Icons.favorite_outline),
                title: const Text('Be a Safe Place'),
                subtitle: const Text('Listen, connect, ask, and repair'),
                trailing: const Icon(Icons.chevron_right),
                onTap: () => Navigator.push(context,
                    MaterialPageRoute(builder: (_) =>
                        const FamilyCompassScreen(forPartner: true))),
              ),
            ),
            Card(
              child: ListTile(
                leading: const Icon(Icons.emoji_events_outlined),
                title: const Text('Build Confidence'),
                subtitle: const Text('Connect, notice effort, and encourage courage'),
                trailing: const Icon(Icons.chevron_right),
                onTap: () => Navigator.push(context,
                    MaterialPageRoute(builder: (_) =>
                        const FamilyCompassScreen(forPartner: false))),
              ),
            ),
            const SizedBox(height: 20),
            const Text('Today: Be someone they can come to.'),
          ],
        ),
      ),
    );
  }
}
